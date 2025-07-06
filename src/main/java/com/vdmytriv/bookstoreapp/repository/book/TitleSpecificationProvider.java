package com.vdmytriv.bookstoreapp.repository.book;

import com.vdmytriv.bookstoreapp.model.Book;
import com.vdmytriv.bookstoreapp.repository.SpecificationProvider;
import jakarta.persistence.criteria.Predicate;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TitleSpecificationProvider implements SpecificationProvider<Book> {

    private static final String KEY_TITLE = "title";

    @Override
    public String getFilterKey() {
        return KEY_TITLE;
    }

    @Override
    public Specification<Book> buildSpecification(String[] params) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.or(
                Arrays.stream(params)
                        .map(param -> criteriaBuilder.like(
                                criteriaBuilder.lower(root.get(KEY_TITLE)),
                                "%" + param.toLowerCase() + "%"
                        ))
                        .toArray(Predicate[]::new));
    }
}
