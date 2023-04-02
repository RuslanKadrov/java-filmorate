package ru.yandex.practicum.model;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Objects;

@Data
public class User {

    private int id;
    private final String email;
    private final String login;
    private final String name;
    private final LocalDate birthday;
}
