package com.vdmytriv.bookstoreapp.controller;

import com.vdmytriv.bookstoreapp.dto.BookDto;
import com.vdmytriv.bookstoreapp.dto.BookSearchParametersDto;
import com.vdmytriv.bookstoreapp.dto.CreateBookRequestDto;
import com.vdmytriv.bookstoreapp.dto.PartialUpdateBookRequestDto;
import com.vdmytriv.bookstoreapp.dto.UpdateBookRequestDto;
import com.vdmytriv.bookstoreapp.service.BookService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    @GetMapping
    public List<BookDto> getAll() {
        return bookService.findAll();
    }

    @GetMapping("/{id}")
    public BookDto getBookById(@PathVariable Long id) {
        return bookService.findById(id);
    }

    @GetMapping("/search")
    public List<BookDto> searchBooks(@Valid BookSearchParametersDto params) {
        return bookService.search(params);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public BookDto createBook(@RequestBody @Valid CreateBookRequestDto requestDto) {
        return bookService.save(requestDto);
    }

    @PutMapping("/{id}")
    public BookDto updateBook(
            @PathVariable Long id,
            @RequestBody @Valid UpdateBookRequestDto requestDto) {
        return bookService.update(id, requestDto);
    }

    @PatchMapping("/{id}")
    public BookDto patchBook(
            @PathVariable Long id,
            @RequestBody @Valid PartialUpdateBookRequestDto requestDto) {
        return bookService.patch(id,requestDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteBookById(@PathVariable Long id) {
        bookService.delete(id);
    }
}
