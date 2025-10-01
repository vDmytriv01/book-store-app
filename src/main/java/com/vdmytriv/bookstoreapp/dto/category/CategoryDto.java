package com.vdmytriv.bookstoreapp.dto.category;

import io.swagger.v3.oas.annotations.media.Schema;

public record CategoryDto(
        @Schema(description = "Unique identifier of the category",
                example = "1")
        Long id,

        @Schema(description = "Name of the category",
                example = "Fiction")

        String name,

        @Schema(description = "Description of the category",
                example = "Fiction books")
        String description
) {
}
