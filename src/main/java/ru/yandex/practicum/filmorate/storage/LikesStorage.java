package ru.yandex.practicum.filmorate.storage;

import java.util.List;
public interface LikesStorage {

    void createLike (long userId, long filmId);
    List<Long> findFilmLikesByUser(long userId);

    List<Long> findLikedUsersByFilm(long filmId);
    void deleteLike (long userId, long filmId);

}
