package com.vdmytriv.bookstoreapp.repository.book;

import com.vdmytriv.bookstoreapp.dto.BookSearchParametersDto;
import com.vdmytriv.bookstoreapp.model.Book;
import com.vdmytriv.bookstoreapp.repository.SpecificationBuilder;
import com.vdmytriv.bookstoreapp.repository.SpecificationProviderManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookSpecificationBuilder
        implements SpecificationBuilder<Book, BookSearchParametersDto> {

    private final SpecificationProviderManager<Book> specificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParametersDto searchParameters) {
        Specification<Book> specification = (
                root, query, cb) -> cb.conjunction();
        if (searchParameters.author() != null && searchParameters.author().length > 0) {
            specification = specification.and(
                    specificationProviderManager.getSpecificationProvider("author")
                            .buildSpecification(searchParameters.author()));
        }
        if (searchParameters.title() != null && searchParameters.title().length > 0) {
            specification = specification.and(
                    specificationProviderManager.getSpecificationProvider("title")
                            .buildSpecification(searchParameters.title()));
        }
        if (searchParameters.isbn() != null && searchParameters.isbn().length > 0) {
            specification = specification.and(
                    specificationProviderManager.getSpecificationProvider("isbn")
                            .buildSpecification(searchParameters.isbn()));
        }
        if (searchParameters.minPrice() != null) {
            specification = specification.and(
                    (root, query, criteriaBuilder) ->
                            criteriaBuilder.greaterThanOrEqualTo(root.get("price"),
                                    searchParameters.minPrice())
            );
        }
        if (searchParameters.maxPrice() != null) {
            specification = specification.and(
                    (root, query, criteriaBuilder) ->
                            criteriaBuilder.lessThanOrEqualTo(root.get("price"),
                                    searchParameters.maxPrice())
            );
        }
        return specification;
    }
}
