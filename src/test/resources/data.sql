insert into users (email, login, name, birthday)
values('ivan@mail.ru', 'ivan111', 'Ivan Ivanov', '2000-12-12');
insert into users (email, login, name, birthday)
values('petya@mail.ru', 'petya111', 'Petya Petrov', '2002-12-12');
insert into users (email, login, name, birthday)
values('valya@mail.ru', 'valya111', 'Valya Valentina', '2004-12-12');

insert into films (name, description, release_date, duration, rating_id)
values('film1', 'description1', '1999-10-11', 200, 1);
insert into films (name, description, release_date, duration, rating_id)
values('film2', 'description2', '1999-10-11', 222, 2);
insert into films (name, description, release_date, duration, rating_id)
values('film3', 'description3', '1999-10-11', 22, 3);
insert into films (name, description, release_date, duration, rating_id)
values('film4', 'description4', '1999-10-11', 200, 1);


insert into films_genres (film_id, genre_id)
values(4, 1);
insert into films_genres (film_id, genre_id)
values(4, 3);