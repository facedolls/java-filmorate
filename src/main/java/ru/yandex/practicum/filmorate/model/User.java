package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import ru.yandex.practicum.filmorate.validator.Login;
import java.time.LocalDate;

@Data
public class User {
    private Integer id;
    @NotNull
    @NotBlank
    @Email
    private String email;
    @NotNull
    @NotBlank
    @Login
    private String login;
    private String name;
    @NotNull
    @Past
    private LocalDate birthday;
}