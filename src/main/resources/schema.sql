DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS friendship CASCADE;
DROP TABLE IF EXISTS genre CASCADE;
DROP TABLE IF EXISTS rating CASCADE;
DROP TABLE IF EXISTS film CASCADE;
DROP TABLE IF EXISTS film_genre CASCADE;
DROP TABLE IF EXISTS favorite_film CASCADE;
DROP TABLE IF EXISTS review CASCADE;
DROP TABLE IF EXISTS review_likes CASCADE;
DROP TABLE IF EXISTS review_dislikes CASCADE;
DROP TABLE IF EXISTS director CASCADE;
DROP TABLE IF EXISTS film_director CASCADE;
DROP TABLE IF EXISTS feed CASCADE;
DROP TABLE IF EXISTS film_mark CASCADE;

CREATE TABLE IF NOT EXISTS users
(
    user_id  INTEGER generated BY DEFAULT AS IDENTITY PRIMARY KEY,
    name     VARCHAR(100) NOT NULL,
    email    VARCHAR(254) NOT NULL,
    login    VARCHAR(100) NOT NULL,
    birthday DATE
);

CREATE TABLE IF NOT EXISTS friendship
(
    user_id   INTEGER NOT NULL,
    friend_id INTEGER NOT NULL,
    status    BOOLEAN,
    PRIMARY KEY (user_id, friend_id),
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE,
    FOREIGN KEY (friend_id) REFERENCES users (user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS genre
(
    genre_id INTEGER generated BY DEFAULT AS IDENTITY PRIMARY KEY,
    name     VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS rating
(
    rating_id   INTEGER generated BY DEFAULT AS IDENTITY PRIMARY KEY,
    name        VARCHAR(20)  NOT NULL UNIQUE,
    description VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS film
(
    film_id      INTEGER generated BY DEFAULT AS IDENTITY PRIMARY KEY,
    name         VARCHAR(200) NOT NULL,
    description  VARCHAR(200) NOT NULL,
    release_date DATE,
    duration     INTEGER NOT NULL,
    rating_id    INTEGER NOT NULL,
    FOREIGN KEY (rating_id) REFERENCES rating (rating_id) ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS film_genre
(
    film_id  INTEGER NOT NULL,
    genre_id INTEGER NOT NULL,
    PRIMARY KEY (film_id, genre_id),
    FOREIGN KEY (film_id) REFERENCES film (film_id) ON DELETE CASCADE,
    FOREIGN KEY (genre_id) REFERENCES genre (genre_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS favorite_film
(
    film_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    grade INTEGER NOT NULL CHECK(grade > 0 AND grade <= 10),
    is_positive BOOLEAN NOT NULL,
    PRIMARY KEY (film_id, user_id),
    FOREIGN KEY (film_id) REFERENCES film (film_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS film_mark
(
    film_id INTEGER NOT NULL UNIQUE,
    mark NUMERIC(4, 2) NOT NULL,
    PRIMARY KEY (film_id),
    FOREIGN KEY (film_id) REFERENCES film (film_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS review
(
    review_id   INTEGER generated BY DEFAULT AS IDENTITY PRIMARY KEY,
    film_id     INTEGER NOT NULL,
    user_id     INTEGER NOT NULL,
    is_positive BOOLEAN,
    content     VARCHAR(500) NOT NULL,
    useful      INTEGER DEFAULT 0,
    FOREIGN KEY (film_id) REFERENCES film (film_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS review_likes
(
    review_id INTEGER NOT NULL,
    user_id   INTEGER NOT NULL,
    is_like BOOLEAN NOT NULL,
    PRIMARY KEY (review_id, user_id),
    FOREIGN KEY (review_id) REFERENCES review (review_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS director
(
    director_id INTEGER generated BY DEFAULT AS IDENTITY PRIMARY KEY,
    name        VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS film_director
(
    film_id     INTEGER NOT NULL,
    director_id INTEGER NOT NULL,
    PRIMARY KEY (film_id, director_id),
    FOREIGN KEY (film_id) REFERENCES film (film_id) ON DELETE CASCADE,
    FOREIGN KEY (director_id) REFERENCES director (director_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS feed
(
    event_id   INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    user_id    INTEGER,
    timestamp  BIGINT      NOT NULL,
    event_type VARCHAR(10) NOT NULL,
    operation  VARCHAR(10) NOT NULL,
    entity_id  INTEGER     NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);

CREATE OR REPLACE FUNCTION update_film_mark() RETURNS TRIGGER AS
'
DECLARE
    new_mark FLOAT;
BEGIN
    IF TG_OP = ''INSERT'' OR TG_OP = ''UPDATE'' THEN
        SELECT AVG(grade) INTO new_mark FROM favorite_film WHERE film_id = NEW.film_id;
        IF new_mark IS NOT NULL THEN
            INSERT INTO film_mark (film_id, mark)
            VALUES (NEW.film_id, new_mark)
            ON CONFLICT (film_id)
            DO UPDATE SET mark = EXCLUDED.mark;
        END IF;
    ELSIF TG_OP = ''DELETE'' THEN
        IF (SELECT COUNT(*) FROM favorite_film WHERE film_id = OLD.film_id) = 0 THEN
            DELETE FROM film_mark WHERE film_id = OLD.film_id;
        ELSE
            SELECT AVG(grade) INTO new_mark FROM favorite_film WHERE film_id = OLD.film_id;
            UPDATE film_mark SET mark = new_mark WHERE film_id = OLD.film_id;
        END IF;
    END IF;
    RETURN NEW;
END;
'
LANGUAGE plpgsql;

CREATE TRIGGER trigger_grade_change
AFTER INSERT OR UPDATE OR DELETE ON favorite_film
FOR EACH ROW
EXECUTE FUNCTION update_film_mark();