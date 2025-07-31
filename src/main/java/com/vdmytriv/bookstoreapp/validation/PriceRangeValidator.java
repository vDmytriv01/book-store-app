package com.vdmytriv.bookstoreapp.validation;

import com.vdmytriv.bookstoreapp.dto.book.BookSearchParametersDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

public class PriceRangeValidator
        implements ConstraintValidator<
        PriceRangeValidation, BookSearchParametersDto> {

    @Override
    public boolean isValid(
            BookSearchParametersDto dto,
            ConstraintValidatorContext constraintValidatorContext) {
        BigDecimal min = dto.minPrice();
        BigDecimal max = dto.maxPrice();

        if (min != null && max != null) {
            return min.compareTo(max) <= 0;
        }
        return true;
    }
}
