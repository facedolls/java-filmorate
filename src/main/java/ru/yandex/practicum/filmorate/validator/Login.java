package ru.yandex.practicum.filmorate.validator;

import javax.validation.Constraint;
import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LoginValidator.class)
public @interface Login {
    String message() default "Login does not meet the criteria {value}";
    Class<?>[] groups() default {};
    Class<?>[] payload() default {};
    String value() default " ";
}
