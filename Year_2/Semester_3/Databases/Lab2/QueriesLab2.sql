--USE StreamingMovies;



-------------------------------------------------
-- INSERT STATEMENTS

-- Directors
INSERT INTO Directors (DirectorId, DirectorName, DirectorBirthday)
VALUES (1, 'John Smith', '1970-08-15'), (2, 'Michael Son', '1980-10-10'), (3, 'Laura Young', '1992-04-06');

-- Writers
INSERT INTO Writers (WriterID, WriterName, WriterBirthday)
VALUES (1, 'Jordan Snow', '1965-03-12'), (2, 'Philip Datt', '1985-11-12'), (3, 'Maria Trent', '1992-04-06');

-- Movies
INSERT INTO Movies (MovieID, DirectorID, WriterID, MovieTitle, MovieReleaseDate, MovieRating)
VALUES (1, 1, 1, 'The Dark Knight', '2008-07-14', 9.0),
	   (2, 2, 2, 'Inception', '2010-07-13', 8.7),
	   (3, 3, 3, 'Joker', '2019-08-31', 8.4),
	   (4, 2, 1, 'Aquaman and the Lost Kingdom', NULL, NULL),
	   (5, 2, 3, 'The room', '2003-03-22', 3.7);

-- Users
INSERT INTO Users (UserID, UserName, UserBirthday, UserCountry, UserWatchHours)
VALUES (1, 'Razvan Dumitru', '2002-04-06', 'Romania', 23),
	   (2, 'Adrian Constantin', '2001-05-21', 'Hungary', 19),
	   (3, 'Bogdan Voicu', '2003-01-19', 'Romania', 18),
	   (4, 'Dragos Mihai', '1999-06-04', 'France', 22),
	   (5, 'Nicolae Popescu', '1999-06-04', 'France', 5);

-- WatchedList
INSERT INTO WatchedList (UserID, MovieID)
VALUES (1, 3), (2, 3), (3, 3), (1, 2), (4, 1);

-- Violates integrity constraints - no matching movie for MovieID -----------------------
-- INSERT INTO WatchedList (UserID, MovieID) VALUES (1, 5);

-- WishList
INSERT INTO WishList (UserID, MovieID)
VALUES (1, 4);

-- Favourites
INSERT INTO Favourites (UserID, MovieID)
VALUES (1, 3), (2, 3);

-- Actors
INSERT INTO Actors(ActorID, ActorName, ActorBirthday, ActorIncome)
VALUES (1, 'Leonardo DiCaprio', '1974-11-11', 3000000),
	   (2, 'Christian Bale', '1974-01-31', 2700000),
	   (3, 'Heath Ledger', '1979-01-22', 2300000),
	   (4, 'Joaquin Phoenix', '1974-08-28', 4300000),
	   (5, 'Chris Evans', '1981-06-13', 3400000);

-- Acts
INSERT INTO Acts (ActorID, MovieID)
VALUES (1, 2), (3, 2), (2, 1), (3, 1), (4, 3);

-- Reviews
INSERT INTO Reviews (UserID, MovieID, ReviewText)
VALUES (1, 1, 'Cool movie, recommend'),
	   (2, 2, 'Something really nice'),
	   (3, 2, 'Pretty interesting'),
	   (1, 2, 'What the heck was that?'),
	   (3, 3, 'This movie is shit');

-- Genres
INSERT INTO Genres (GenreID, GenreDescription)
VALUES (1, 'Romance'),
	   (2, 'Action'),
	   (3, 'Comedy'),
	   (4, 'Thriller'),
	   (5, 'Drama');

-- GenreTypes
INSERT INTO GenreTypes (MovieID, GenreID)
VALUES (1, 2), (1, 4), (1, 5), (2, 2), (2, 4), (3, 5);



-------------------------------------------------
-- UPDATE STATEMENTS

-- Update: add 30 000 dolars to the income for the actors born in 1974
-- used: =
SELECT * FROM Actors;

UPDATE Actors
SET ActorIncome = ActorIncome + 30000
WHERE YEAR(ActorBirthday) = 1974; 

SELECT * FROM Actors;

-- Update: increase with 2 the watch hours of the users from romania or hungary that were born between 2000 and 2005
--		   and their names end with 'u'
-- used: AND, IN, BETWEEN, LIKE
SELECT * FROM Users;

UPDATE Users
SET UserWatchHours = UserWatchHours + 2
WHERE UserCountry IN ('Romania', 'Hungary') AND YEAR(UserBirthday) BETWEEN 2000 AND 2005 AND UserName LIKE '%u';

SELECT * FROM Users;

-- Update: update the release date to 2024-01-01 for the movies that have NULL as a date
-- used: IS NULL
SELECT * FROM Movies;

UPDATE Movies
SET MovieReleaseDate = '2024-01-01'
WHERE MovieReleaseDate IS NULL;

SELECT * FROM Movies;



-------------------------------------------------
-- DELETE STATEMENTS

-- Delete: delete reviews that contain the words 'heck' or 'shit'
-- used: LIKE, OR
SELECT * FROM Reviews;

DELETE FROM Reviews
WHERE ReviewText LIKE '%shit%' OR ReviewText LIKE '%heck%';

SELECT * FROM Reviews;

-- Delete: delete movies that have the rating smaller than 5
-- used: <
SELECT * FROM Movies;

DELETE FROM Movies
WHERE MovieRating < 5;

SELECT * FROM Movies;



-------------------------------------------------
-- QUERIES

-- a) 2 queries with the union operation; use UNION [ALL] and OR
-- find the ids of the users that are from romania or reviewed the movie with id 2
SELECT U.UserID
FROM Users U
WHERE U.UserCountry = 'romania'
UNION
SELECT R.UserID
FROM Reviews R
WHERE R.MovieID = 2;

-- find the actor names that played in the movie with id 1 or the movie with id 2
SELECT DISTINCT A.ActorName
FROM Actors A, Acts Ac
WHERE (A.ActorID = Ac.ActorID AND Ac.MovieID = 1) OR (A.ActorID = Ac.ActorID AND Ac.MovieID = 2);


-- b) 2 queries with the intersection operation; use INTERSECT and IN
-- find the user names who watched both the movies with id 2 and 3
SELECT U.UserName
FROM Users U, WatchedList WL
WHERE U.UserID = WL.UserID AND WL.MovieID = 2
INTERSECT
SELECT U.UserName
FROM Users U, WatchedList WL
WHERE U.UserID = WL.UserID AND WL.MovieID = 3

-- find the user names who watched both the movies with id 2 and 3 (with IN)
SELECT U.UserName
FROM Users U, WatchedList WL
WHERE U.UserID = WL.UserID AND WL.MovieID = 2 AND U.UserID IN (SELECT UserID 
															   FROM WatchedList 
															   WHERE MovieID = 3);


-- c) 2 queries with the difference operation; use EXCEPT and NOT IN
-- find the actor ids who played in the movie with id 1 and not in the movie with id 2
SELECT Ac.ActorID
FROM Acts Ac
WHERE Ac.MovieID = 1
EXCEPT
SELECT Ac1.ActorID
FROM Acts Ac1
WHERE Ac1.MovieID = 2;

-- find the actor ids who played in the movie with id 1 and not in the movie with id 2 (with NOT IN)
SELECT Ac.ActorID
FROM Acts Ac
WHERE Ac.MovieID = 1 AND Ac.ActorID NOT IN (SELECT Ac1.ActorID
										    FROM Acts Ac1
											WHERE Ac1.MovieID = 2);


-- d) 4 queries with INNER JOIN, LEFT JOIN, RIGHT JOIN, and FULL JOIN (one query per operator); 
--    one query will join at least 3 tables, while another one will join at least two many-to-many relationships

-- INNER JOIN 3 tables
-- find the movies that have associated genres, showing also the genres
SELECT M.MovieTitle, G.GenreDescription
FROM Movies M
INNER JOIN GenreTypes GT ON M.MovieID = GT.MovieID
INNER JOIN Genres G ON GT.GenreID = G.GenreID
GROUP BY M.MovieTitle, G.GenreDescription;

-- FULL JOIN 2 many to many relationships
-- find all the actor names with all the movie titles that they played in and all the possible reviews 
-- and the user names that added the reviews
SELECT A.ActorName, M.MovieTitle, R.ReviewText, U.UserName
FROM Actors A
FULL JOIN Acts Ac ON Ac.ActorID = A.ActorID
FULL JOIN Movies M ON M.MovieID = Ac.MovieID
FULL JOIN Reviews R ON R.MovieID = M.MovieID
FULL JOIN Users U ON U.UserID = R.UserID;

-- LEFT JOIN 
-- find all the movies with the possible watchers
SELECT M.MovieTitle, U.UserName
FROM Movies M
LEFT JOIN WatchedList WL ON M.MovieID = WL.MovieID
LEFT JOIN Users U ON WL.UserID = U.UserID;

-- RIGHT JOIN
-- find the movie titles and the user names that reviewed a movie, including also the users that did not
-- review any movie
SELECT M.MovieTitle, U.UserName
FROM Movies M
RIGHT JOIN Reviews R ON M.MovieID = R.MovieID
RIGHT JOIN Users U ON U.UserID = R.UserID;




-- e)  2 queries with the IN operator and a subquery in the WHERE clause; in at least one case, 
--     the subquery must include a subquery in its own WHERE clause

-- the first case is somewhere above

-- 2 subqueries
-- find the name of the actors who played in the movie with the title 'Inception'
SELECT A.ActorName
FROM Actors A
WHERE A.ActorID IN (SELECT Ac.ActorID
				    FROM Acts Ac
					WHERE Ac.MovieID IN (SELECT M.MovieID
										 FROM Movies M
										 WHERE M.MovieTitle = 'Inception'));



-- f) 2 queries with the EXISTS operator and a subquery in the WHERE clause

-- add 20 000 to the income for the actor with id 1
SELECT A.ActorName, A.ActorIncome + 20000 AS NewIncome
FROM Actors A
WHERE EXISTS (SELECT * FROM Actors A1 WHERE A1.ActorID = 1 AND A.ActorID = A1.ActorID);

-- add 3 to the watched hours of the users who watched the movie with id 3
SELECT U.UserName, U.UserWatchHours + 3 as NewUserWatchHours
FROM Users U
WHERE EXISTS (SELECT * 
			  FROM Movies M 
			  INNER JOIN WatchedList WL ON M.MovieID = WL.MovieID
			  WHERE WL.UserID = U.UserID AND M.MovieID = 3);



-- g) 2 queries with a subquery in the FROM clause

-- find the names and the incomes for the top 2 actors that have an income smaller than 3 000 000 and give them 10 000, 
-- ordered ascendant by the income
SELECT TOP 2 A1.ActorName, A1.ActorIncome + 10000 AS NewIncome
FROM (SELECT *
	  FROM Actors A
	  WHERE A.ActorIncome < 3000000) A1
ORDER BY A1.ActorIncome DESC;

-- find the best payed actor
SELECT TOP 1 A.ActorName, A.ActorIncome
FROM (SELECT *
	  FROM Actors) A
ORDER BY A.ActorIncome DESC;



-- h) 4 queries with the GROUP BY clause, 3 of which also contain the HAVING clause;
--    2 of the latter will also have a subquery in the HAVING clause; 
--    use the aggregation operators: COUNT, SUM, AVG, MIN, MAX;

-- find the countries with minimum 2 users from it
SELECT U.UserCountry, COUNT(*) AS NumberOfUsers
FROM Users U
GROUP BY U.UserCountry
HAVING COUNT(*) >= 2;

-- find the movies with the most actors in it
SELECT M.MovieTitle, COUNT(*) AS NumberOfActors
FROM Acts Ac
INNER JOIN Movies M ON M.MovieID = Ac.MovieID
GROUP BY M.MovieTitle
HAVING COUNT(*) = (SELECT MAX(N.NumActors)
				   FROM (SELECT COUNT(*) AS NumActors
						 FROM Acts A
						 GROUP BY A.MovieID) N);

-- find the actors whose income is below average
SELECT DISTINCT A.ActorName, A.ActorIncome
FROM Actors A
GROUP BY A.ActorName, A.ActorIncome
HAVING A.ActorIncome < (SELECT AVG(A1.ActorIncome)
						FROM Actors A1);
SELECT AVG(A.ActorIncome) FROM Actors A;

-- find how much value every movie has from its actor incomes
SELECT M.MovieTitle, SUM(A.ActorIncome) AS ValueOfActors
FROM Movies M
INNER JOIN Acts Ac ON M.MovieID = Ac.MovieID
INNER JOIN Actors A ON Ac.ActorID = A.ActorID
GROUP BY M.MovieTitle
ORDER BY ValueOfActors ASC;



-- i) 4 queries using ANY and ALL to introduce a subquery in the WHERE clause (2 queries per operator); 
--    rewrite 2 of them with aggregation operators, and the other 2 with IN / [NOT] IN

-- find the user names whose watch hours are smaller than all watch hours of the users from romania
-- used: ALL
SELECT DISTINCT U.UserName
FROM Users U
WHERE U.UserWatchHours < ALL(SELECT DISTINCT U1.UserWatchHours
							 FROM Users U1
							 WHERE U1.UserCountry = 'Romania');

-- find the user names whose watch hours are smaller than all watch hours of the users from romania
-- rewritten with: MIN
SELECT DISTINCT U.UserName
FROM Users U
WHERE U.UserWatchHours < (SELECT MIN(U1.UserWatchHours)
						  FROM Users U1
						  WHERE U1.UserCountry = 'Romania');

-- find all the reviews for the top movies (i.e. the rating >= 8.5)
-- used: ANY
SELECT R.*
FROM Reviews R
WHERE R.MovieID = ANY(SELECT M.MovieID
					  FROM Movies M
					  WHERE M.MovieRating >= 8.5);

-- find all the reviews for the top movies (i.e. the rating >= 8.5)
-- rewritten with: IN
SELECT R.*
FROM Reviews R
WHERE R.MovieID IN (SELECT M.MovieID
					FROM Movies M
					WHERE M.MovieRating >= 8.5);

-- find the actor names who played in at least one movie
-- used: ANY
SELECT DISTINCT A.ActorName
FROM Actors A
WHERE A.ActorID = ANY(SELECT Ac.ActorID
					  FROM Acts Ac);

-- find the actor names who played in at least one movie
-- rewritten with: IN
SELECT DISTINCT A.ActorName
FROM Actors A
WHERE A.ActorID IN (SELECT Ac.ActorID
					  FROM Acts Ac);

-- find the top paid actors
-- used: ALL
SELECT DISTINCT A.ActorName
FROM Actors A
WHERE A.ActorIncome >= ALL(SELECT A1.ActorIncome
						  FROM Actors A1
						  WHERE A1.ActorID <> A.ActorID);

-- find the top paid actors
-- rewritten with: MAX
SELECT DISTINCT A.ActorName
FROM Actors A
WHERE A.ActorIncome >= (SELECT MAX(A1.ActorIncome)
					   FROM Actors A1
					   WHERE A1.ActorID <> A.ActorID);



-- arithmetic expressions in the SELECT clause in at least 3 queries - lines 251, 256, 268
-- conditions with AND, OR, NOT, and parentheses in the WHERE clause in at least 3 queries - lines 155, 171, 189
-- DISTINCT in at least 3 queries - lines 153, 303, 325
-- ORDER BY in at least 2 queries - lines 272, 278
-- TOP in at least 2 queries - lines 268, 275