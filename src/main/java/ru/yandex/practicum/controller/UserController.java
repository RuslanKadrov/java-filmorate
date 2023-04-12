package ru.yandex.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.Exceptions.ValidationException;
import ru.yandex.practicum.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();

    private int counter = 0;

    private int createId() {
        return ++counter;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User addUser(@RequestBody User user) throws ValidationException {
        checkData(user);
        if (user.getName() == null) {
            user = setNameForUserByLogin(user);
            user.setId(createId());
            users.put(user.getId(), user);
        } else if (user.getName().isBlank()) {
            user = setNameForUserByLogin(user);
            user.setId(createId());
            users.put(user.getId(), user);
        } else {
            user.setId(createId());
            users.put(user.getId(), user);
        }
        log.info("Добавлен новый пользователь \"{}\"", user.getName());
        return users.get(user.getId());
    }

    @PutMapping
    public User updateUser(@RequestBody User user) throws ValidationException {

        checkData(user);
        if (users.containsKey(user.getId())) {
            if (user.getName().isBlank() || user.getName() == null) {
                user = setNameForUserByLogin(user);
                users.replace(user.getId(), user);
            } else {
                users.replace(user.getId(), user);
            }
        } else {
            log.warn("Ошибка обновления пользователя \"{}\"", user.getName());
            throw new ValidationException();
        }
        log.info("Пользователь с ником \"{}\" успешно обновлен", user.getName());
        return user;
    }

    private User setNameForUserByLogin(User us) {
        return new User(us.getEmail(), us.getLogin(), us.getLogin(), us.getBirthday());
    }

    private void checkData(User user) throws ValidationException {
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.warn("Пустой/неверный email");
            throw new ValidationException();
        } else if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.warn("Отсутствует логин");
            throw new ValidationException();
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Неправильная дата рождения");
            throw new ValidationException();
        }
    }
}
