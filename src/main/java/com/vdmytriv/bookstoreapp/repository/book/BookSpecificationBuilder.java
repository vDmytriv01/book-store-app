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

    private static final String KEY_AUTHOR = "author";
    private static final String KEY_TITLE = "title";
    private static final String KEY_ISBN = "isbn";
    private static final String KEY_PRICE = "price";

    private final SpecificationProviderManager<Book> specificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParametersDto searchParameters) {
        Specification<Book> specification = (
                root, query, cb) -> cb.conjunction();
        if (searchParameters.author() != null && searchParameters.author().length > 0) {
            specification = specification.and(
                    specificationProviderManager.getSpecificationProvider(KEY_AUTHOR)
                            .buildSpecification(searchParameters.author()));
        }
        if (searchParameters.title() != null && searchParameters.title().length > 0) {
            specification = specification.and(
                    specificationProviderManager.getSpecificationProvider(KEY_TITLE)
                            .buildSpecification(searchParameters.title()));
        }
        if (searchParameters.isbn() != null && searchParameters.isbn().length > 0) {
            specification = specification.and(
                    specificationProviderManager.getSpecificationProvider(KEY_ISBN)
                            .buildSpecification(searchParameters.isbn()));
        }
        if (searchParameters.minPrice() != null) {
            specification = specification.and(
                    (root, query, criteriaBuilder) ->
                            criteriaBuilder.greaterThanOrEqualTo(root.get(KEY_PRICE),
                                    searchParameters.minPrice())
            );
        }
        if (searchParameters.maxPrice() != null) {
            specification = specification.and(
                    (root, query, criteriaBuilder) ->
                            criteriaBuilder.lessThanOrEqualTo(root.get(KEY_PRICE),
                                    searchParameters.maxPrice())
            );
        }
        return specification;
    }
}
