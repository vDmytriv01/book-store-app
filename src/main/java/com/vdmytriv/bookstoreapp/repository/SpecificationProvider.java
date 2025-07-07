package com.vdmytriv.bookstoreapp.repository;

import org.springframework.data.jpa.domain.Specification;

public interface SpecificationProvider<T> {
    String getFilterKey();

    Specification<T> buildSpecification(String[] params);
}
