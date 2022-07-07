package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.FilmorateValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import javax.validation.*;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class FilmorateApplicationTests {

	private static ValidatorFactory validatorFactory;
	private static Validator validator;
	FilmService filmService;
	UserService userService;

	@BeforeAll
	public static void createValidator() {
		validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
	}

	@AfterAll
	public static void close() {
		validatorFactory.close();
	}

	@BeforeEach
	public void createServiceObjects (){
		filmService = new FilmService(new InMemoryFilmStorage(), new InMemoryUserStorage());
		userService = new UserService(new InMemoryUserStorage());
	}

	@Test
	void contextLoads() {
	}

	@Test
	void createUser(){
		User user = new User("innam@test.ru", "innam", "Inna Murzina", LocalDate.of(1975, 12, 25));
		UserController controller = new UserController(userService);
		controller.create(user);
		assertEquals(1,controller.findAll().size() );
		User result = controller.findAll().iterator().next();
		assertEquals ("innam@test.ru", result.getEmail());
		assertEquals ("innam", result.getLogin());
		assertEquals ("Inna Murzina", result.getName());
		assertEquals (LocalDate.of(1975,12,25), result.getBirthday());
		assertEquals (1, result.getId());

	}

	@Test
	void updateUserWithValidId(){
		User user = new User("innam@test.ru", "innam", "Inna Murzina", LocalDate.of(1975, 12, 25));
		UserController controller = new UserController(userService);
		controller.create(user);
		User updateUser = new User("updated@test.ru", "updatedlogin", "Updated Name", LocalDate.of(1985, 1, 20));
		updateUser.setId(1);
		controller.update(updateUser);
		assertEquals(1,controller.findAll().size() );
		User result = controller.findAll().iterator().next();
		assertEquals ("updated@test.ru", result.getEmail());
		assertEquals ("updatedlogin", result.getLogin());
		assertEquals ("Updated Name", result.getName());
		assertEquals (LocalDate.of(1985,1,20), result.getBirthday());
		assertEquals (1, result.getId());

	}

	@Test
	void createFilm(){
		Film film = new Film ("Man in Black", "Best movie ever", LocalDate.of(1997, 1,1),90);
		FilmController controller = new FilmController(filmService);
		controller.create(film);
		assertEquals(1,controller.findAll().size() );
		Film result = controller.findAll().iterator().next();
		assertEquals ("Man in Black", result.getName());
		assertEquals ("Best movie ever", result.getDescription());
		assertEquals (LocalDate.of(1997,1,1), result.getReleaseDate());
		assertEquals (90, result.getDuration());
		assertEquals (1, result.getId());
	}


	@Test
	void updateFilmWithValidId(){
		Film film = new Film ("Man in Black", "Best movie ever", LocalDate.of(1997, 1,1),90);
		FilmController controller = new FilmController(filmService);
		controller.create(film);
		Film updateFilm = new Film ("Updated film name", "Updated film description", LocalDate.of(2000, 2,2),100);
		updateFilm.setId(1);
		controller.update(updateFilm);
		assertEquals(1,controller.findAll().size() );
		Film result = controller.findAll().iterator().next();
		assertEquals ("Updated film name", result.getName());
		assertEquals ("Updated film description", result.getDescription());
		assertEquals (LocalDate.of(2000,2,2), result.getReleaseDate());
		assertEquals (100, result.getDuration());
		assertEquals (1, result.getId());

	}

	@Test
	void updateFilmWithNegativeIdShouldThrowException(){
		Film film = new Film ("Man in Black", "Best movie ever", LocalDate.of(1997, 1,1),90);
		FilmController controller = new FilmController(filmService);
		controller.create(film);
		Film updateFilm = new Film ("Updated film name", "Updated film description", LocalDate.of(2000, 2,2),100);
		updateFilm.setId(-1);
		Exception exception = assertThrows(
				FilmorateValidationException.class,
				() -> controller.update(updateFilm)
		);

		assertEquals("неверный ID фильма для обновления -1", exception.getMessage());

	}

	@Test
	void updateUserWithNegativeIdShouldThrowException(){
		User user = new User("innam@test.ru", "innam", "Inna Murzina", LocalDate.of(1975, 12, 25));
		UserController controller = new UserController(userService);
		controller.create(user);
		User updateUser = new User("updated@test.ru", "updatedlogin", "Updated Name", LocalDate.of(1985, 1, 20));
		updateUser.setId(-1);
		Exception exception = assertThrows(
				FilmorateValidationException.class,
				() -> controller.update(updateUser)
		);

		assertEquals("неверный ID пользователя для обновления -1", exception.getMessage());

	}

	@Test
	public void shouldHaveNoUserViolations() {

		User user = new User("innam@test.ru", "innam", "Inna Murzina", LocalDate.of(1975, 12, 25));
		Set<ConstraintViolation<User>> violations = validator.validate(user);
		assertTrue(violations.isEmpty());
	}

	@Test
	public void shouldHaveNoFilmViolations() {

		Film film = new Film ("Man in Black", "Best movie ever ", LocalDate.of(1997, 1,1),90);
		Set<ConstraintViolation<Film>> violations = validator.validate(film);
		assertTrue(violations.isEmpty());
	}

	@Test
	public void EmptyFilmNameViolation() {
		Film film = new Film (" ", "Best movie ever ", LocalDate.of(1997, 1,1),90);
		Set<ConstraintViolation<Film>> violations = validator.validate(film);
		assertEquals(violations.size(), 1);
		ConstraintViolation<Film> violation
				= violations.iterator().next();
		assertEquals("название фильма не должно быть пустым",
				violation.getMessage());
		assertEquals("name", violation.getPropertyPath().toString());
	}

	@Test
	public void FilmDescriptionLength201Violation() {
		Film film = new Film ("Man in Black",
				"BestMovieBestMovieBestMovieBestMovieBestMovieBestMovieBestMovieBestMovieBestMovie" +
						"BestMovieBestMovieBestMovieBestMovieBestMovieBestMovieBestMovieBestMovieBestMovieBestMovie" +
						"BestMovieBestMovieBestMovieEnd", LocalDate.of(1997, 1,1),90);
		Set<ConstraintViolation<Film>> violations = validator.validate(film);
		assertEquals(violations.size(), 1);
		ConstraintViolation<Film> violation
				= violations.iterator().next();
		assertEquals("описание фильма должно быть не длиннее 200 символов",
				violation.getMessage());
		assertEquals("description", violation.getPropertyPath().toString());
	}

	@Test
	public void FilmDescriptionLength200NotViolation() {
		Film film = new Film ("Man in Black",
				"BestMovieBestMovieBestMovieBestMovieBestMovieBestMovieBestMovieBestMovieBestMovie" +
						"BestMovieBestMovieBestMovieBestMovieBestMovieBestMovieBestMovieBestMovieBestMovieBestMovie" +
						"BestMovieBestMovieBestMovieEn", LocalDate.of(1997, 1,1),90);
		Set<ConstraintViolation<Film>> violations = validator.validate(film);
		assertTrue(violations.isEmpty());
	}

	@Test
	public void FilmReleaseDate_27_12_1895_ShouldThrowException() {
		Film film = new Film ("Man in Black",
		"Best Movie Ever", LocalDate.of(1895, 12,27),90);
		FilmController controller = new FilmController(filmService);

		Exception exception = assertThrows(
				FilmorateValidationException.class,
				() -> controller.create(film)
		);

		assertEquals("Дата фильма не может быть раньше 28.12.1895", exception.getMessage());
	}

	@Test
	public void FilmReleaseDate_28_12_1895_NotThrowException() {
		Film film = new Film ("Man in Black",
				"Best Movie Ever", LocalDate.of(1895, 12,28),90);
		FilmController controller = new FilmController(filmService);
		assertDoesNotThrow(() -> controller.create(film));
	}

	@Test
	public void FilmNegativeDurationViolation() {
		Film film = new Film ("Man in Black",
				"Best Movie ever", LocalDate.of(1997, 1,1),-1);
		Set<ConstraintViolation<Film>> violations = validator.validate(film);
		assertEquals(violations.size(), 1);
		ConstraintViolation<Film> violation
				= violations.iterator().next();
		assertEquals("продолжительность фильма должна быть больше 0",
				violation.getMessage());
		assertEquals("duration", violation.getPropertyPath().toString());
	}

	@Test
	public void EmptyUserEmailViolation() {
		User user = new User ("", "imurzina","Inna Murzina", LocalDate.of(1975, 1, 25));
		Set<ConstraintViolation<User>> violations = validator.validate(user);
		assertEquals(violations.size(), 1);
		ConstraintViolation<User> violation
				= violations.iterator().next();
		assertEquals("email не может быть пустым",
				violation.getMessage());
		assertEquals("email", violation.getPropertyPath().toString());
	}

	@Test
	public void WrongUserEmailViolations() {
		User user = new User ("hdcj", "imurzina","Inna Murzina", LocalDate.of(1975, 1, 25));
		Set<ConstraintViolation<User>> violations = validator.validate(user);
		ConstraintViolation<User> violation
				= violations.iterator().next();

		assertEquals("неправильный формат email",
				violation.getMessage());
		assertEquals("email", violation.getPropertyPath().toString());
	}

	@Test
	public void EmptyUserLoginViolations() {
		User user = new User ("innam@test.com", "","Inna Murzina", LocalDate.of(1975, 1, 25));
		Set<ConstraintViolation<User>> violations = validator.validate(user);
		ConstraintViolation<User> violation
				= violations.iterator().next();

		assertEquals("логин не может быть пустым",
				violation.getMessage());
		assertEquals("login", violation.getPropertyPath().toString());
	}

	@Test
	public void UserLoginWithSpacesViolations() {
		User user = new User ("innam@test.com", "Inna Inna","Inna Murzina", LocalDate.of(1975, 1, 25));
		Set<ConstraintViolation<User>> violations = validator.validate(user);
		ConstraintViolation<User> violation
				= violations.iterator().next();

		assertEquals("в логине не должно быть пробелов",
				violation.getMessage());
		assertEquals("login", violation.getPropertyPath().toString());
	}

	@Test
	public void EmptyUserNameShouldUserLoginAsName() {
		User user = new User ("innam@test.com", "imurzina","", LocalDate.of(1975, 1, 25));
		UserController controller = new UserController(userService);
		User returnedUser = controller.create(user);
		assertEquals("imurzina",
				returnedUser.getName());
	}

	@Test
	public void UserBirthdayInFutureViolations() {
		User user = new User ("innam@test.com", "innam","Inna Murzina", LocalDate.of(2022, 12, 30));
		Set<ConstraintViolation<User>> violations = validator.validate(user);
		ConstraintViolation<User> violation
				= violations.iterator().next();

		assertEquals("дата рождения не может быть в будущем",
				violation.getMessage());
		assertEquals("birthday", violation.getPropertyPath().toString());
	}


}
