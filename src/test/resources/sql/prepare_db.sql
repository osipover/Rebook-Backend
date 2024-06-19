INSERT INTO t_author (id, first_name, last_name, middle_name, birth_date) VALUES
    (1, 'Александр', 'Пушкин', '', '1799-06-06');

INSERT INTO t_book(id, title, description, publication_date, rating) VALUES
    (1, 'Евгений Онегин', 'Описание', 1833, 8),
    (2, 'Капитанская дочка', 'Описание', 1833, 7);

INSERT INTO t_book_author(book_id, author_id) VALUES
     (1, 1),
     (2, 1);

INSERT INTO t_user(id, email, password, first_name, last_name, date_of_creation) VALUES
     (1, 'ivan@ivan', 'ivan', 'Ivan', 'Ivanov', '2024-10-10'),
     (2, 'peter@peter', 'peter', 'Peter', 'Parker', '2024-10-10');

INSERT INTO t_review(id, user_id, book_id, title, description, date_of_creation) VALUES
    (1, 1, 1, 'Заголовок 1', 'Текст 1', now()),
    (2, 2, 1, 'Заголовок 2', 'Текст 2', now());

INSERT INTO t_rating(id, user_id, book_id, rating) VALUES
    (1, 1, 1, 6),
    (2, 2, 1, 10);