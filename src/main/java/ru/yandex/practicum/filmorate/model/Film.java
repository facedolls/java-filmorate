package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.yandex.practicum.filmorate.validator.date.AfterMinDate;
import java.time.LocalDate;
import java.util.*;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
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
    private List<Genre> genres = new ArrayList<>();
    private List<Director> directors = new ArrayList<>();

    public Film(String name, String description, LocalDate releaseDate, int duration,
                RatingMpa mpa, List<Genre> genres, List<Director> directors) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        this.genres = genres;
        this.directors = directors;
    }
}