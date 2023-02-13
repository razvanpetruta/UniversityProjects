--CREATE DATABASE StreamingMovies;

--USE StreamingMovies;

DROP TABLE IF EXISTS GenreTypes;
DROP TABLE IF EXISTS Genres;
DROP TABLE IF EXISTS Acts;
DROP TABLE IF EXISTS Reviews;
DROP TABLE IF EXISTS WatchedList;
DROP TABLE IF EXISTS WishList;
DROP TABLE IF EXISTS Favourites;
DROP TABLE IF EXISTS Movies;
DROP TABLE IF EXISTS Directors;
DROP TABLE IF EXISTS Writers;
DROP TABLE IF EXISTS Users;
DROP TABLE IF EXISTS Actors;

CREATE TABLE Directors(
	DirectorID INT PRIMARY KEY NOT NULL,
	DirectorName NVARCHAR(50) NOT NULL,
	DirectorBirthday DATE NOT NULL
);

CREATE TABLE Writers(
	WriterID INT PRIMARY KEY NOT NULL,
	WriterName NVARCHAR(50) NOT NULL,
	WriterBirthday DATE NOT NULL
);

-- 1:n   |   one director can direct n(movies)   |   one movie can be directed by 1(director)
-- 1:n   |   one writer can script n(movies)     |   one movie can be scripted by 1(writer)
CREATE TABLE Movies(
	MovieID INT PRIMARY KEY NOT NULL,
	DirectorID INT FOREIGN KEY REFERENCES Directors(DirectorID) NOT NULL,
	WriterID INT FOREIGN KEY REFERENCES Writers(WriterID) NOT NULL,
	MovieTitle NVARCHAR(50) NOT NULL,
	MovieReleaseDate DATE,
	MovieRating FLOAT
);

CREATE TABLE Users(
	UserID INT PRIMARY KEY NOT NULL,
	UserName NVARCHAR(50) NOT NULL,
	UserBirthday DATE NOT NULL,
	UserCountry NVARCHAR(50) NOT NULL,
	UserWatchHours INT
);

-- n:m   |   one user can watch n(movies)   |   one movie can be watched by m(users)
CREATE TABLE WatchedList(
	UserID INT FOREIGN KEY REFERENCES Users(UserID) NOT NULL,
	MovieID INT FOREIGN KEY REFERENCES Movies(MovieID) NOT NULL,
	CONSTRAINT PKWatchedListUserMovie PRIMARY KEY (UserID, MovieID)
);

-- n:m   |   one user can wishlist n(movies)   |   one movie can be wishlisted by m(users)
CREATE TABLE WishList(
	UserID INT FOREIGN KEY REFERENCES Users(UserID) NOT NULL,
	MovieID INT FOREIGN KEY REFERENCES Movies(MovieID) NOT NULL,
	CONSTRAINT PKWishListUserMovie PRIMARY KEY (UserID, MovieID)
);

-- n:m   |   one user can add to favourites n(movies)   |   one movie can be added to favourites by m(users)
CREATE TABLE Favourites(
	UserID INT FOREIGN KEY REFERENCES Users(UserID) NOT NULL,
	MovieID INT FOREIGN KEY REFERENCES Movies(MovieID) NOT NULL,
	CONSTRAINT PKFavouritesUserMovie PRIMARY KEY (UserID, MovieID)
);

CREATE TABLE Actors(
	ActorID INT PRIMARY KEY NOT NULL,
	ActorName NVARCHAR(50) NOT NULL,
	ActorBirthday DATE NOT NULL,
	ActorIncome FLOAT NOT NULL
);

-- n:m   |   one actor can act in n(movies)   |   one movie can have m(actors)
CREATE TABLE Acts(
	ActorID INT FOREIGN KEY REFERENCES Actors(ActorID) NOT NULL,
	MovieID INT FOREIGN KEY REFERENCES Movies(MovieID) NOT NULL,
	CONSTRAINT PKActsActorMovie PRIMARY KEY (ActorID, MovieID)
);

-- 1:n   |   one user can write n(reviews)   |   one review can be written by 1(user)
-- 1:n   |   one movie can have n(reviews)   |   one review can be associated to 1(movie)
CREATE TABLE Reviews(
	UserID INT FOREIGN KEY REFERENCES Users(UserID) NOT NULL,
	MovieID INT FOREIGN KEY REFERENCES Movies(MovieID) NOT NULL,
	ReviewText NVARCHAR(50) NOT NULL,
	CONSTRAINT PKReviewMovieUser PRIMARY KEY (MovieID, UserID)
);

CREATE TABLE Genres(
	GenreID INT PRIMARY KEY NOT NULL,
	GenreDescription NVARCHAR(50) NOT NULL
);

-- n:m   |   one movie can have n(genres)   |   one genre can belong to m(movies)
CREATE TABLE GenreTypes(
	MovieID INT FOREIGN KEY REFERENCES Movies(MovieID) NOT NULL,
	GenreID INT FOREIGN KEY REFERENCES Genres(GenreID) NOT NULL,
	CONSTRAINT PKGenreTypesGenreMovie PRIMARY KEY (GenreID, MovieID)
);