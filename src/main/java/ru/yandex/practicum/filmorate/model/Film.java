package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.yandex.practicum.filmorate.validator.AfterMinDate;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Film {
    private Integer id;
    @NotNull
    @NotBlank
    private String name;
    @NotNull
    @Size(max = 200)
    private String description;
    @NotNull
    @AfterMinDate
    private LocalDate releaseDate;
    @Positive
    private int duration;
}