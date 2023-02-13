-- a view with a SELECT statement that operates on at least 2 different tables and contains at least one JOIN operator

GO
CREATE OR ALTER VIEW actionStartsWithA AS
	SELECT M.MovieTitle, G.GenreDescription
	FROM Movies M
	INNER JOIN GenreTypes GT ON GT.MovieID = M.MovieID
	INNER JOIN Genres G ON GT.GenreID = G.GenreID
	WHERE G.GenreDescription LIKE 'a%'


GO
EXEC addToTables 'GenreTypes'
EXEC addToTables 'Movies'
EXEC addToTables 'Genres'
EXEC addToTables 'Writers'
EXEC addToTables 'Directors'
EXEC addToViews 'actionStartsWithA'
EXEC addToTests 'test3'
EXEC connectTableToTest 'Writers', 'test3', 100, 1
EXEC connectTableToTest 'Directors', 'test3', 100, 2
EXEC connectTableToTest 'Movies', 'test3', 100, 3
EXEC connectTableToTest 'Genres', 'test3', 100, 4
EXEC connectTableToTest 'GenreTypes', 'test3', 100, 5
EXEC connectViewToTest 'actionStartsWithA', 'test3'


GO
CREATE OR ALTER PROCEDURE populateTableGenres (@numberOfRows INT) AS
	DECLARE @stringValue VARCHAR(50)
	WHILE @numberOfRows > 0 
	BEGIN
		EXEC generateRandomString @stringValue OUTPUT
		INSERT INTO Genres (GenreID, GenreDescription)
		VALUES (@numberOfRows, @stringValue)
		SET @numberOfRows = @numberOfRows - 1
	END


GO
CREATE OR ALTER PROCEDURE populateTableGenreTypes (@numberOfRows INT) AS
	DECLARE @genreID INT
	DECLARE @movieID INT
	WHILE @numberOfRows > 0 
	BEGIN
		SET @genreID = (SELECT TOP 1 GenreID FROM Genres ORDER BY NEWID())
		SET @movieID = (SELECT TOP 1 MovieID FROM Movies ORDER BY NEWID())
		WHILE EXISTS (SELECT * FROM GenreTypes WHERE MovieID = @movieID AND GenreID = @genreID)
		BEGIN
			SET @genreID = (SELECT TOP 1 GenreID FROM Genres ORDER BY NEWID())
			SET @movieID = (SELECT TOP 1 MovieID FROM Movies ORDER BY NEWID())
		END
		INSERT INTO GenreTypes(MovieID, GenreID)
		VALUES (@movieID, @genreID)
		SET @numberOfRows = @numberOfRows - 1
	END