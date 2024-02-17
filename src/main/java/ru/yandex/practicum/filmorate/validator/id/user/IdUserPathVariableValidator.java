package ru.yandex.practicum.filmorate.validator.id.user;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
public class IdUserPathVariableValidator implements ConstraintValidator<CorrectUserId, Long> {
    @Override
    public void initialize(CorrectUserId constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long id, ConstraintValidatorContext constraintValidatorContext) {
        if (id == null || id < 1) {
            log.warn("Incorrect id={} passed for user", id);
            throw new IncorrectParameterException(String.format("Error with field id=%d for user", id));
        }
        return true;
    }
}
