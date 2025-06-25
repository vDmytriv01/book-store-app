package com.vdmytriv.bookstoreapp.repository;

import com.vdmytriv.bookstoreapp.exception.DataProcessingException;
import com.vdmytriv.bookstoreapp.model.Book;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class BookRepositoryImpl implements BookRepository {

    private final EntityManagerFactory entityManagerFactory;

    @Override
    @Transactional
    public Book save(Book book) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            entityManager.persist(book);
            return book;
        } catch (PersistenceException e) {
            throw new DataProcessingException("Couldn't save book " + book, e);
        }
    }

    @Override
    public List<Book> findAll() {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return entityManager
                    .createQuery("SELECT b FROM Book b", Book.class)
                    .getResultList();
        } catch (PersistenceException e) {
            throw new DataProcessingException("Couldn't find books ", e);
        }
    }

    @Override
    public Optional<Book> findById(Long id) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            Book book = entityManager.find(Book.class, id);
            return Optional.ofNullable(book);
        } catch (PersistenceException e) {
            throw new DataProcessingException("Couldn't find book " + id, e);
        }
    }
}
