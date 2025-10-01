package com.vdmytriv.bookstoreapp.repository.category;

import com.vdmytriv.bookstoreapp.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends
        JpaRepository<Category, Long> {
}
