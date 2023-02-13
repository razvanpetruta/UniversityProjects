-- a view with a SELECT statement that has a GROUP BY clause, 
--		operates on at least 2 different tables and contains at least one JOIN operator
-- a table with a single-column primary key and at least one foreign key
-- a table with a multicolumn primary key

GO
CREATE OR ALTER VIEW moviesAndReviews AS
	SELECT M.MovieTitle, R.ReviewText
	FROM Movies M 
	INNER JOIN Reviews R ON M.MovieID = R.MovieID
	INNER JOIN Users U ON U.UserID = R.UserID
	GROUP BY M.MovieTitle, R.ReviewText


GO
EXEC addToTables 'Reviews'
EXEC addToTables 'Movies'
EXEC addToTables 'Users'
EXEC addToTables 'Writers'
EXEC addToTables 'Directors'
EXEC addToViews 'moviesAndReviews'
EXEC addToTests 'test2'
EXEC connectTableToTest 'Writers', 'test2', 100, 1
EXEC connectTableToTest 'Directors', 'test2', 100, 2
EXEC connectTableToTest 'Movies', 'test2', 100, 3
EXEC connectTableToTest 'Users', 'test2', 100, 4
EXEC connectTableToTest 'Reviews', 'test2', 100, 5
EXEC connectViewToTest 'moviesAndReviews', 'test2'


GO
CREATE OR ALTER PROCEDURE populateTableDirectors (@numberOfRows INT) AS
	DECLARE @stringValue VARCHAR(50)
	WHILE @numberOfRows > 0 
	BEGIN
		EXEC generateRandomString @stringValue OUTPUT
		INSERT INTO Directors (DirectorId, DirectorName, DirectorBirthday) 
		VALUES (@numberOfRows, @stringValue, '2002-05-05')
		SET @numberOfRows = @numberOfRows - 1
	END


GO
CREATE OR ALTER PROCEDURE populateTableWriters (@numberOfRows INT) AS
	DECLARE @stringValue VARCHAR(50)
	WHILE @numberOfRows > 0 
	BEGIN
		EXEC generateRandomString @stringValue OUTPUT
		INSERT INTO Writers (WriterID, WriterName, WriterBirthday)
		VALUES (@numberOfRows, @stringValue, '2002-05-05')
		SET @numberOfRows = @numberOfRows - 1
	END


GO
CREATE OR ALTER PROCEDURE populateTableMovies (@numberOfRows INT) AS
	DECLARE @numberOfRowsCopy INT
	SET @numberOfRowsCopy = @numberOfRows
	DECLARE @stringValue VARCHAR(50)
	WHILE @numberOfRows > 0 
	BEGIN
		EXEC generateRandomString @stringValue OUTPUT
		INSERT INTO Movies (MovieID, DirectorID, WriterID, MovieTitle, MovieReleaseDate, MovieRating)
		VALUES (@numberOfRows, floor(rand() * @numberOfRowsCopy) + 1, floor(rand() * @numberOfRowsCopy) + 1, @stringValue, '2010-10-10', 9)
		SET @numberOfRows = @numberOfRows - 1
	END


GO
CREATE OR ALTER PROCEDURE populateTableUsers (@numberOfRows INT) AS
	DECLARE @stringValue VARCHAR(50)
	WHILE @numberOfRows > 0 
	BEGIN
		EXEC generateRandomString @stringValue OUTPUT
		INSERT INTO Users (UserID, UserName, UserBirthday, UserCountry, UserWatchHours)
		VALUES (@numberOfRows, @stringValue, '2002-10-10', @stringValue, 10)
		SET @numberOfRows = @numberOfRows - 1
	END


GO
CREATE OR ALTER PROCEDURE populateTableReviews (@numberOfRows INT) AS
	DECLARE @stringValue VARCHAR(50)
	DECLARE @userID INT
	DECLARE @movieID INT
	WHILE @numberOfRows > 0 
	BEGIN
		EXEC generateRandomString @stringValue OUTPUT
		SET @userID = (SELECT TOP 1 UserID FROM Users ORDER BY NEWID())
		SET @movieID = (SELECT TOP 1 MovieID FROM Movies ORDER BY NEWID())
		WHILE EXISTS (SELECT * FROM Reviews WHERE MovieID = @movieID AND UserID = @userID)
		BEGIN
			SET @userID = (SELECT TOP 1 UserID FROM Users ORDER BY NEWID())
			SET @movieID = (SELECT TOP 1 MovieID FROM Movies ORDER BY NEWID())
		END
		INSERT INTO Reviews(UserID, MovieID, ReviewText)
		VALUES (@userID, @movieID, @stringValue)
		SET @numberOfRows = @numberOfRows - 1
	END