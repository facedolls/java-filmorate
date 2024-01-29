package ru.yandex.practicum.filmorate.validator;

import jakarta.validation.*;
import lombok.extern.slf4j.Slf4j;
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
