INSERT INTO users (user_id, email, login, name, birthday)
VALUES (1, 'yandex@yandex.ru', 'Login1', 'Name1', '1998-03-07');

INSERT INTO users (user_id, email, login, name, birthday)
VALUES (2, 'yandex@yandex.ru', 'Login2', 'Name2', '1998-03-07');

INSERT INTO users (user_id, email, login, name, birthday)
VALUES (3, 'yandex@yandex.ru', 'Login3', 'Name3', '1998-03-07');

INSERT INTO film (film_id, name, description, release_date, duration, rating_id)
VALUES (1, 'First film', 'First film', '2008-01-01', 155, 1);

INSERT INTO film (film_id, name, description, release_date, duration, rating_id)
VALUES (2, 'Second film', 'Second film', '1996-09-10', 133, 2);

INSERT INTO film (film_id, name, description, release_date, duration, rating_id)
VALUES (3, 'Last film', 'Last film', '2005-05-05', 199, 1);

INSERT INTO review (content, is_positive, user_id, film_id, useful)
VALUES ('Good film', true, 1, 1, 1);

INSERT INTO review (content, is_positive, user_id, film_id, useful)
VALUES ('Bad film', false, 2, 2, 0);

INSERT INTO feed (timestamp, user_id, event_type, operation, entity_id)
VALUES (1234, 1, 'LIKE', 'ADD', 1);

INSERT INTO feed (timestamp, user_id, event_type, operation, entity_id)
VALUES (4321, 1, 'FRIEND', 'ADD', 2);

INSERT INTO feed (timestamp, user_id, event_type, operation, entity_id)
VALUES (2345, 1, 'REVIEW', 'ADD', 1);

INSERT INTO feed (timestamp, user_id, event_type, operation, entity_id)
VALUES (5432, 2, 'REVIEW', 'ADD', 2);