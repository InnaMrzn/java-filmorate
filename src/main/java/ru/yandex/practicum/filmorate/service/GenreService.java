package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;

import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;

@Service
@Slf4j
public class GenreService {
    private final GenreStorage genreStorage;
    public GenreService(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public List<Genre> findAll(){
        return genreStorage.getGenres();
    }

    public Genre findById  (int id){

        return genreStorage.getGenreById(id);
    }
}
