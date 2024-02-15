package ru.yandex.practicum.filmorate.validator.date;

import javax.validation.Constraint;
import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MinimumDateValidator.class)
public @interface AfterMinDate {
    String message() default "Date must not be before {value}";
    Class<?>[] groups() default {};
    Class<?>[] payload() default {};
    String value() default "";
}
