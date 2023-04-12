package ru.yandex.practicum.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class User {

    private int id;
    private final String email;
    private final String login;
    private final String name;
    private final LocalDate birthday;
}
