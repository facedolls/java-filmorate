package ru.yandex.practicum.filmorate.validator.login;

import lombok.extern.slf4j.Slf4j;
import javax.validation.*;

@Slf4j
public class LoginValidator implements ConstraintValidator<Login, String> {
    private String value;

    @Override
    public void initialize(Login constraintAnnotation) {
        value = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(String login, ConstraintValidatorContext constraintValidatorContext) {
        if (login.contains(value)) {
            log.warn("An exception was thrown for {}", login);
            return false;
        }
        return true;
    }
}
