package ru.yandex.practicum.filmorate.validator.count;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
public class CountFilmPathVariableValidator implements ConstraintValidator<CorrectFilmCount, Integer> {
    @Override
    public void initialize(CorrectFilmCount constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Integer count, ConstraintValidatorContext constraintValidatorContext) {
        if (count < 1) {
            log.warn("Incorrect count={} passed for films", count);
            throw new IncorrectParameterException(String.format("Error with field count=%d", count));
        }
        return true;
    }
}
