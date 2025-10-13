SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE books_categories;
TRUNCATE TABLE categories;
SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO categories (id, name, description) VALUES (1, 'Programming', 'Tech books');

