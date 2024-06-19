CREATE TABLE t_author
(
    id          BIGSERIAL NOT NULL,
    first_name  TEXT      NOT NULL,
    last_name   TEXT      NOT NULL,
    middle_name TEXT,
    birth_date  DATE,

    PRIMARY KEY (id)
);

CREATE TABLE t_genre
(
    id          BIGSERIAL NOT NULL,
    title       TEXT      NOT NULL UNIQUE,
    description TEXT,

    PRIMARY KEY (id)
);

CREATE TABLE t_book
(
    id               BIGSERIAL NOT NULL,
    title            TEXT      NOT NULL,
    description      TEXT,
    publication_date INT,
    rating           FLOAT,

    PRIMARY KEY (id)
);

CREATE TABLE t_book_author
(
    book_id   BIGINT NOT NULL,
    author_id BIGINT NOT NULL,

    PRIMARY KEY (book_id, author_id),
    FOREIGN KEY (book_id) REFERENCES t_book (id) ON UPDATE CASCADE,
    FOREIGN KEY (author_id) REFERENCES t_author (id) ON DELETE CASCADE
);

CREATE TABLE t_book_genre
(
    book_id  BIGINT NOT NULL,
    genre_id BIGINT NOT NULL,

    PRIMARY KEY (book_id, genre_id)
);

CREATE TABLE t_role
(
    id   BIGSERIAL NOT NULL,
    name TEXT      NOT NULL CHECK (length(trim(name)) > 0) UNIQUE,

    PRIMARY KEY (id)
);

CREATE TABLE t_user
(
    id               BIGSERIAL NOT NULL,
    email            TEXT      NOT NULL UNIQUE,
    password         TEXT      NOT NULL,
    first_name       TEXT      NOT NULL,
    last_name        TEXT      NOT NULL,
    date_of_creation DATE,

    PRIMARY KEY (id)
);

CREATE TABLE t_user_role
(
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,

    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES t_user (id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES t_role (id) ON DELETE CASCADE
);

CREATE TABLE t_rating
(
    id      BIGSERIAL NOT NULL,
    user_id BIGINT    NOT NULL,
    book_id BIGINT    NOT NULL,
    rating  FLOAT     NOT NULL CHECK (1 <= rating AND rating <= 10),

    PRIMARY KEY (id),
    UNIQUE (user_id, book_id),
    FOREIGN KEY (user_id) REFERENCES t_user (id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES t_book (id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE t_review
(
    id               BIGSERIAL NOT NULL,
    user_id          BIGINT    NOT NULL,
    book_id          BIGINT    NOT NULL,
    title            TEXT      NOT NULL,
    description      TEXT      NOT NULL,
    date_of_creation TIMESTAMP NOT NULL,

    PRIMARY KEY (id),
    UNIQUE (user_id, book_id),
    FOREIGN KEY (user_id) REFERENCES t_user (id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES t_book (id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE t_collection
(
    id          BIGINT NOT NULL,
    title       TEXT   NOT NULL UNIQUE,
    description TEXT   NOT NULL,

    PRIMARY KEY (id)
);

CREATE TABLE t_book_collection
(
    book_id       BIGINT NOT NULL,
    collection_id BIGINT NOT NULL,

    PRIMARY KEY (book_id, collection_id)
)