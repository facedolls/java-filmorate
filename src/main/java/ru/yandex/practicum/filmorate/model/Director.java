package ru.yandex.practicum.filmorate.model;

import lombok.*;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class Director {
    private Integer id;
    @NotBlank
    private String name;
}