package com.vdmytriv.bookstoreapp.repository.book;

import com.vdmytriv.bookstoreapp.model.Book;
import com.vdmytriv.bookstoreapp.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class AuthorSpecificationProvider implements SpecificationProvider<Book> {

    private static final String KEY_AUTHOR = "author";

    @Override
    public String getFilterKey() {
        return KEY_AUTHOR;
    }

    @Override
    public Specification<Book> buildSpecification(String[] params) {
        return ((root, query, criteriaBuilder) ->
                root.get(KEY_AUTHOR).in((Object[]) params));
    }
}
