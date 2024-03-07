package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.filmorate.validator.date.AfterMinDate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@RequiredArgsConstructor
public class Film {
    private int id;
    @NotBlank
    private String name;
    @NotBlank
    @Size(max = 200)
    private String description;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    @AfterMinDate(value = "1895-12-28")
    private LocalDate releaseDate;
    @NotNull
    @Positive
    private int duration;
    @NotNull
    private RatingMpa mpa;
    private Set<Long> like = new HashSet<>();
    private List<Genre> genres = new ArrayList<>();

    public Film(String name, String description, LocalDate releaseDate, int duration, RatingMpa mpa) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
    }

    public Film(String name, String description, LocalDate releaseDate, int duration,
                RatingMpa mpa, List<Genre> genres) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        this.genres = genres;
    }

    public Film(int id, String name, String description, LocalDate releaseDate, int duration,
                RatingMpa mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
    }
}