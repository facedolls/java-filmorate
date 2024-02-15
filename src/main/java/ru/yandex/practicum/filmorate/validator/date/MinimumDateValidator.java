package ru.yandex.practicum.filmorate.validator.date;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.validator.date.AfterMinDate;

import javax.validation.*;
import java.time.LocalDate;

@Slf4j
public class MinimumDateValidator implements ConstraintValidator<AfterMinDate, LocalDate> {
    private LocalDate minDate;

    @Override
    public void initialize(AfterMinDate constraintAnnotation) {
        minDate = LocalDate.parse(constraintAnnotation.value());
    }

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        if (localDate.isBefore(minDate)) {
            log.warn("An exception was thrown for {}", localDate);
            return false;
        }
        return true;
    }
}
