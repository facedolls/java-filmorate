package ru.yandex.practicum.filmorate.validator.id.film;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
public class IdFilmPathVariableValidator implements ConstraintValidator<CorrectFilmId, Integer> {
    @Override
    public void initialize(CorrectFilmId constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Integer id, ConstraintValidatorContext constraintValidatorContext) {
        if (id == null || id < 1) {
            log.warn("Incorrect id={} passed for film", id);
            throw new IncorrectParameterException(String.format("Error with field id=%d for film", id));
        }
        return true;
    }
}
