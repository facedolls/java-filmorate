package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.yandex.practicum.filmorate.validator.login.Login;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.*;

@Data
@RequiredArgsConstructor
public class User {
    private long id;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Login(value = " ")
    private String login;
    private String name;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Past(message = "The date of birth cannot be the future")
    private LocalDate birthday;
    private Set<Long> friends = new HashSet<>();

    public User(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    public User(long id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }
}