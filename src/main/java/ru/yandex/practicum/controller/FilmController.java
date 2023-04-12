package ru.yandex.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.Exceptions.ValidationException;
import ru.yandex.practicum.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Integer, Film> films = new HashMap<>();

    private int counter = 0;

    private int createId() {
        return ++counter;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) throws ValidationException {
        checkData(film);
        film.setId(createId());
        films.put(film.getId(), film);
        log.info("Фильм с названием \"{}\" успешно добавлен", film.getName());
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) throws ValidationException {
        checkData(film);
        if (films.containsKey(film.getId())) {
            films.replace(film.getId(), film);
        } else {
            log.warn("Ошибка обновления фильма \"{}\"", film.getName());
            throw new ValidationException();
        }
        log.info("Фильм с названием \"{}\" успешно обновлен", film.getName());
        return film;
    }

    private void checkData(Film film) throws ValidationException {
        if (film.getName().isBlank()) {
            log.warn("Отсутствует название фильма");
            throw new ValidationException();
        } else if (film.getDescription().length() > 200) {
            log.warn("Описание превышает допустимое количество символов");
            throw new ValidationException();
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Неверная дата релиза");
            throw new ValidationException();
        } else if (film.getDuration() < 0) {
            log.warn("Продолжительность не может быть отрицательной");
            throw new ValidationException();
        }
    }
}
