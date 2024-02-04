package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.yandex.practicum.filmorate.validator.AfterMinDate;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Film {
    private Integer id;
    @NotBlank
    private String name;
    @NotBlank
    @Size(max = 200)
    private String description;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    @AfterMinDate(value = "1895-12-28")
    private LocalDate releaseDate;
    @Positive
    private int duration;
}