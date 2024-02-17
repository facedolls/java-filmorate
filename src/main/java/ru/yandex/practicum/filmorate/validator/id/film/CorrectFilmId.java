package ru.yandex.practicum.filmorate.validator.id.film;

import javax.validation.Constraint;
import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IdFilmPathVariableValidator.class)
public @interface CorrectFilmId {
    String message() default "Id must not be empty and less than 1";
    Class<?>[] groups() default {};
    Class<?>[] payload() default {};
    String value() default "";
}
