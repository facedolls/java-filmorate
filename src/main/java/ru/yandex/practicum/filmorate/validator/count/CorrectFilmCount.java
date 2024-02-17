package ru.yandex.practicum.filmorate.validator.count;

import javax.validation.Constraint;
import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CountFilmPathVariableValidator.class)
public @interface CorrectFilmCount {
    String message() default "The quantity must not be negative";
    Class<?>[] groups() default {};
    Class<?>[] payload() default {};
    String value() default "";
}
