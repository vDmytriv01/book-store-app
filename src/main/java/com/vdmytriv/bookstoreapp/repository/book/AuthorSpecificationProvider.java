package com.vdmytriv.bookstoreapp.repository.book;

import com.vdmytriv.bookstoreapp.model.Book;
import com.vdmytriv.bookstoreapp.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class AuthorSpecificationProvider implements SpecificationProvider<Book> {
    @Override
    public String getFilterKey() {
        return "author";
    }

    @Override
    public Specification<Book> buildSpecification(String[] params) {
        return ((root, query, criteriaBuilder) ->
                root.get("author").in((Object[]) params));
    }
}
