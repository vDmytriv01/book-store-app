package com.vdmytriv.bookstoreapp.controller;

import com.vdmytriv.bookstoreapp.dto.book.BookDto;
import com.vdmytriv.bookstoreapp.dto.book.BookSearchParametersDto;
import com.vdmytriv.bookstoreapp.dto.book.CreateBookRequestDto;
import com.vdmytriv.bookstoreapp.dto.book.UpdateBookRequestDto;
import com.vdmytriv.bookstoreapp.service.book.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(
        value = "/books",
        produces = "application/json"
)
@Tag(name = "Books", description = "Endpoints for managing books")
@SecurityRequirement(name = "BearerAuth")
public class BookController {

    private final BookService bookService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    @Operation(summary = "Get all books",
            description = "Retrieves all books with pagination and sorting.")
    public Page<BookDto> getAll(
            @ParameterObject Pageable pageable) {
        return bookService.findAll(pageable);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    @Operation(summary = "Get a book by ID",
            description = "Retrieves a single book by its ID.")
    public BookDto getBookById(
            @Parameter(description = "ID of the book to retrieve") @PathVariable Long id) {
        return bookService.findById(id);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/search")
    @Operation(summary = "Search books",
            description = "Search books by filters with pagination.")
    public Page<BookDto> searchBooks(
            @ParameterObject @Valid BookSearchParametersDto params,
            @ParameterObject Pageable pageable) {
        return bookService.search(params, pageable);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @Operation(summary = "Create a new book",
            description = "Creates a new book.")
    public BookDto createBook(@RequestBody @Valid CreateBookRequestDto requestDto) {
        return bookService.save(requestDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Update a book",
            description = "Replaces all fields of a book.")
    public BookDto updateBook(
            @Parameter(description = "ID of the book to update") @PathVariable Long id,
            @RequestBody @Valid UpdateBookRequestDto requestDto) {
        return bookService.update(id, requestDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a book",
            description = "Deletes a book by ID.")
    public void deleteBookById(
            @Parameter(description = "ID of the book to delete") @PathVariable Long id) {
        bookService.delete(id);
    }
}
