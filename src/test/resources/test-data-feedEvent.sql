INSERT INTO users (email, login, name, birthday)
VALUES ('yandex@yandex,ru', 'login1', 'name1', '1998-03-07');
INSERT INTO users (email, login, name, birthday)
VALUES ('yandex@yandex,ru', 'login2', 'name2', '1998-03-08');
INSERT INTO users (email, login, name, birthday)
VALUES ('yandex@yandex,ru', 'login3', 'name3', '1998-03-09');
INSERT INTO users (email, login, name, birthday)
VALUES ('yandex@yandex,ru', 'login4', 'name4', '1998-03-01');
INSERT INTO users (email, login, name, birthday)
VALUES ('yandex@yandex,ru', 'login5', 'name5', '1998-03-04');

INSERT INTO director (name)
VALUES ('Директор1'),
       ('Директор2'),
       ('Директор3');


INSERT INTO film (name, description, release_date, duration, rating_id)
VALUES ('name1', 'description1', '2007-04-12', 122, 1);
INSERT INTO film (name, description, release_date, duration, rating_id)
VALUES ('name2', 'description2', '2004-11-12', 188, 2);

INSERT INTO film_director (film_id, director_id)
VALUES (1, 1),
       (1, 3),
       (2, 1);