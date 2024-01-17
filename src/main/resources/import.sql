-- Gli statement non devono andare a capo
-- insert books
INSERT INTO books (`year`, created_at, isbn, authors, publisher, title, synopsis,number_of_copies) VALUES(2009, '2024-01-08', '9780132350884', 'Robert C. Martin', 'Pearson', 'Clean Code', 'Manual for developers',2);
INSERT INTO books (`year`, created_at, isbn, authors, publisher, title, synopsis,number_of_copies) VALUES(1973, '2024-01-08', '6580132450884', 'Frank Herbert', 'Einaudi', 'Dune', 'A fantasy saga',3);
-- insert borrowings
INSERT INTO borrowings (book_id, expire_date, return_date, start_date, note) VALUES(1, '2024-01-20', null, '2023-12-20', '');
INSERT INTO borrowings (book_id, expire_date, return_date, start_date, note) VALUES(1, '2023-11-10', '2023-11-11', '2023-10-10', 'Cover is stained');
-- insert book_type
INSERT INTO book_type (name) VALUES('hard cover');
INSERT INTO book_type (name) VALUES('flexible cover');
INSERT INTO book_type (name) VALUES('audiobook');
INSERT INTO book_type (name) VALUES('e-book');
INSERT INTO book_type (name) VALUES('ancient book');