package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Review {
    private long id;
    @NotNull
    private long filmId;
    @NotNull
    private long userId;
    @NotNull
    private boolean isPositive;
    @NotBlank
    @Size(max = 5000)
    private String content;
    private int useful = 0;
}
