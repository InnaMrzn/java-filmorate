package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.*;
import ru.yandex.practicum.filmorate.storage.impl.UserDbStorage;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {
	private final UserDbStorage userStorage;
	private final FilmStorage filmStorage;
	private final LikesStorage likesStorage;
	private final GenreStorage genreStorage;
	private final MpaStorage mpaStorage;
	private final FriendshipStorage friendshipStorage;

	@Test
	public void testUpdateUser(){
		User updatedUser = new User("inna@test.com", "updatedInnam", "Inna Updated",
				LocalDate.of(1999,12,23));
		updatedUser.setId(1L);
		long userId = userStorage.update(updatedUser);
		assertThat(userId).isEqualTo(1L);

	}
	@Test
	public void testFindUserById() {
		User result = userStorage.getUserById(1);
    	assertThat(result).isNotNull().hasFieldOrPropertyWithValue("id", 1L)
				.hasFieldOrPropertyWithValue("name","Inna Updated");
	}

	@Test
	public void testFindUsers() {
		List<User> results = userStorage.getUsers();
		assertThat(results).isNotNull().hasSize(2);
		assertThat(results.get(0)).hasFieldOrPropertyWithValue("id", 1L);
	}
    @Test
	public void testMpaGetAll() {
		List<Mpa> results = mpaStorage.getMpas();
		assertThat(results).isNotNull().hasSize(5);
		assertThat(results.get(0)).hasFieldOrPropertyWithValue("name", "G");
	}

	@Test
	public void testFindMpaById() {
		Mpa result = mpaStorage.getMpaById(1);
		assertThat(result).isNotNull().hasFieldOrPropertyWithValue("id", 1)
				.hasFieldOrPropertyWithValue("name","G");
	}
	@Test
	public void testGenresGetAll() {
		List<Genre> results = genreStorage.getGenres();
		assertThat(results).isNotNull().hasSize(6);
		assertThat(results.get(0)).hasFieldOrPropertyWithValue("name", "Комедия");
	}

	@Test
	public void testFindGenreById() {
		Genre result = genreStorage.getGenreById(1);
		assertThat(result).isNotNull().hasFieldOrPropertyWithValue("id", 1);
	}

		@Test
	public void testUpdateFilm(){
		Film updatedFilm = new Film("My updated film 1", "updated good movie",
				LocalDate.of(1999,12,23), 100);
		updatedFilm.setId(1L);
		updatedFilm.setMpa(mpaStorage.getMpaById(1));
		long filmId = filmStorage.update(updatedFilm);
		assertThat(filmId).isEqualTo(1L);
	}
	@Test
	public void testFindFilmById() {
		Film result = filmStorage.getFilmById(1);
		assertThat(result).isNotNull().hasFieldOrPropertyWithValue("id", 1L)
				.hasFieldOrPropertyWithValue("name","My updated film 1");
	}
	@Test
	public void testFindFilms() {
		List<Film> results = filmStorage.getFilms();
		assertThat(results).isNotNull().hasSize(2);
		assertThat(results.get(0)).hasFieldOrPropertyWithValue("id", 1L);
	}
	@Test
	public void testAddAndDeleteLike() {
		likesStorage.createLike(1L,2L);
		List<Long> likedFilms = likesStorage.findFilmLikesByUser(1L);
		List<Long> userLikes = likesStorage.findLikedUsersByFilm(2L);
		assertThat(likedFilms).hasSize(1);
		assertThat(userLikes).hasSize(1);
		likesStorage.deleteLike(1,2);
		likedFilms = likesStorage.findFilmLikesByUser(1);
		userLikes = likesStorage.findLikedUsersByFilm(2);
		assertThat(likedFilms).hasSize(0);
		assertThat(userLikes).hasSize(0);
	}

	@Test
	public void testGetPopularFilms(){
		likesStorage.createLike(1L,2L);
		List<Film> populrFilms = filmStorage.getPopularFilms(1);
		assertThat(populrFilms).hasSize(1);
		assertThat(populrFilms.get(0)).hasFieldOrPropertyWithValue("id",2L);
	}

	@Test
	public void testAddConfirmDeleteFriend(){
		friendshipStorage.createFriendRequest(1,2);
		List<Long> friends = friendshipStorage.findFriendsByUser(1);
		assertThat(friends).hasSize(1);
		friendshipStorage.confirmFriendRequest(1,2);
		friends = friendshipStorage.findFriendsByUser(2);
		assertThat(friends).hasSize(1);
		friendshipStorage.removeFriendship(1,2);
		List<Long> friends1 = friendshipStorage.findFriendsByUser(1);
		List<Long> friends2 = friendshipStorage.findFriendsByUser(1);
		assertThat(friends1).hasSize(0);
		assertThat(friends2).hasSize(0);
	}



}
