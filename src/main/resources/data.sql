INSERT INTO genre
VALUES (DEFAULT, 'Комедия'),
       (DEFAULT, 'Драма'),
       (DEFAULT, 'Мультфильм'),
       (DEFAULT, 'Триллер'),
       (DEFAULT, 'Документальный'),
       (DEFAULT, 'Боевик');

INSERT INTO rating
VALUES (DEFAULT, 'G', 'у фильма нет возрастных ограничений'),
       (DEFAULT, 'PG', 'детям рекомендуется смотреть фильм с родителями'),
       (DEFAULT, 'PG-13', 'детям до 13 лет просмотр не желателен'),
       (DEFAULT, 'R', 'лицам до 17 лет просматривать фильм можно только в присутствии взрослого'),
       (DEFAULT, 'NC-17', 'лицам до 18 лет просмотр запрещён');

INSERT INTO users (name, email, login, birthday)
VALUES ('test1', 'test@test.ru', 'testlogin', '1987-01-01');

INSERT INTO film (name, description, release_date, duration, rating_id)
VALUES ('testfilm', 'testdescriprion', '2000-01-01', '90', '1');