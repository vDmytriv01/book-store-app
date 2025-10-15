SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE books_categories;
TRUNCATE TABLE books;

SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO books (id, title, author, isbn, price, description, cover_image)
VALUES (
           1,
           'Effective Java',
           'Joshua Bloch',
           '978-3-16-148410-1',
           49.99,
           'A book about Java best practices.',
           'https://example.com/image.jpg'
       );

INSERT INTO books_categories (book_id, category_id) VALUES (1, 1);

