-- Создание таблицы users
CREATE TABLE IF NOT EXISTS users
(
    id       BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email    VARCHAR(255),
    login    VARCHAR(255),
    name     VARCHAR(255),
    birthday DATE
);

-- Создание таблицы mpa
CREATE TABLE IF NOT EXISTS mpa
(
    mpa_id  INT PRIMARY KEY,
    mpa_name    VARCHAR(255)
);

-- Создание таблицы films
CREATE TABLE IF NOT EXISTS films
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name        VARCHAR(255),
    description TEXT,
    releaseDate DATE,
    duration    INTEGER,
    likes       INTEGER DEFAULT 0,
    mpa_id      INT,
    FOREIGN KEY (mpa_id) REFERENCES mpa (mpa_id)
);

-- Создание таблицы friendships
CREATE TABLE IF NOT EXISTS friendships
(
    user_id   BIGINT,
    friend_id BIGINT,
    status varchar DEFAULT 'pending',
    PRIMARY KEY (user_id, friend_id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (friend_id) REFERENCES users (id)
);

-- Создание таблицы likes
CREATE TABLE IF NOT EXISTS likes
(
    user_id BIGINT,
    film_id BIGINT,
    PRIMARY KEY (user_id, film_id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (film_id) REFERENCES films (id)
);

-- Создание таблицы genres
CREATE TABLE IF NOT EXISTS genres
(
    genre_id INT PRIMARY KEY,
    genre    VARCHAR(255)
);

-- Создание таблицы film_genre
CREATE TABLE IF NOT EXISTS film_genre
(
    film_id  BIGINT,
    genre_id INT,
    PRIMARY KEY (film_id, genre_id),
    FOREIGN KEY (film_id) REFERENCES films (id),
    FOREIGN KEY (genre_id) REFERENCES genres (genre_id)
);




