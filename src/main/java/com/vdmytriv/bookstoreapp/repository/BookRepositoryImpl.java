package com.vdmytriv.bookstoreapp.repository;

import com.vdmytriv.bookstoreapp.exception.RepositoryException;
import com.vdmytriv.bookstoreapp.model.Book;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class BookRepositoryImpl implements BookRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Book save(Book book) {
        try {
            entityManager.persist(book);
            return book;
        } catch (PersistenceException e) {
            throw new RepositoryException("Couldn't save book " + book, e);
        }
    }

    @Override
    public List<Book> findAll() {
        try {
            return entityManager
                    .createQuery("SELECT b FROM Book b", Book.class)
                    .getResultList();
        } catch (PersistenceException e) {
            throw new RepositoryException("Couldn't find books ", e);
        }

    }

    @Override
    public Optional<Book> findById(Long id) {
        try {
            Book book = entityManager.find(Book.class, id);
            return Optional.ofNullable(book);
        } catch (PersistenceException e) {
            throw new RepositoryException("Couldn't find book " + id, e);
        }
    }
}
