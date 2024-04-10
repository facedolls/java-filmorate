package ru.yandex.practicum.filmorate.validator.login;

import javax.validation.Constraint;
import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LoginValidator.class)
public @interface Login {
    String message() default "Login must not contain a space";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};

    String value() default "";
}
