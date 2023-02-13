-- CREATE THE TABLES

DROP TABLE Movies;
DROP TABLE Writers;
DROP TABLE Directors;

CREATE TABLE Directors (
	DirectorID INT PRIMARY KEY NOT NULL,
	DirectorCNP INT UNIQUE,
	DirectorNumberOfDirections INT
);

CREATE TABLE Writers (
	WriterID INT PRIMARY KEY NOT NULL,
	WriterNumberOfWritings INT
);

CREATE TABLE Movies (
	MovieID INT PRIMARY KEY,
	DirectorID INT FOREIGN KEY REFERENCES Directors(DirectorID),
	WriterID INT FOREIGN KEY REFERENCES Writers(WriterID),
	MovieTitle NVARCHAR(50)
);


-- GENERATE RANDOM DATA FOR THE TABLES

GO
CREATE OR ALTER PROCEDURE insertIntoDirectors (@rows INT) AS
BEGIN
	DECLARE @aux INT
	SET @aux = @rows + 1
	WHILE @rows > 0
	BEGIN
		INSERT INTO Directors VALUES(@rows, @aux, @aux % 345)
		SET @rows = @rows - 1
		SET @aux = @aux + 1
	END
END

GO
CREATE OR ALTER PROCEDURE insertIntoWriters (@rows INT) AS
BEGIN
	DECLARE @aux INT
	SET @aux = @rows + 1
	WHILE @rows > 0
	BEGIN
		INSERT INTO Writers VALUES(@rows, @aux % 345)
		SET @rows = @rows - 1
		SET @aux = @aux + 1
	END
END

GO
CREATE OR ALTER PROCEDURE insertIntoMovies(@rows INT) AS
BEGIN
	DECLARE @directorID INT
	DECLARE @writerID INT
	WHILE @rows > 0
	BEGIN
		SET @directorID = (SELECT TOP 1 DirectorID FROM Directors ORDER BY NEWID())
		SET @writerID = (SELECT TOP 1 WriterID FROM Writers ORDER BY NEWID())
		INSERT INTO Movies VALUES(@rows, @directorID, @writerID, 'movie' + CAST(@directorID + @writerID AS VARCHAR(10)))
		SET @rows = @rows - 1
	END
END


-- INSERT DATA
EXEC insertIntoDirectors 5000;
EXEC insertIntoWriters 5000;
EXEC insertIntoMovies 5000;

SELECT * FROM Directors;
SELECT * FROM Writers;
SELECT * FROM Movies;


/*
	Remarks (indexes automatically created):
		- clustered index for DirectorID column from Directors
		- unique, nonclustered index for DirectorCNP column from Directors
		- clustered index for WriterID column from Writers
		- clustered index for MovieID column from Movies
*/

-- a)
-- cluster index scan for Directors - scan the entire table
SELECT * 
FROM Directors;

-- cluster index seek for Directors - return a specific subset of rows
SELECT *
FROM Directors
WHERE DirectorID < 100;

-- nonclustered index scan for Directors
SELECT DirectorCNP
FROM Directors;

-- nonclustered index seek for Directors
SELECT DirectorCNP
FROM Directors
WHERE DirectorCNP < 5500;

-- key lookup - nonclustered index seek + key lookup - the data is found in a nonclustered
--				index, but additional data is required
SELECT DirectorCNP, DirectorNumberOfDirections
FROM Directors
WHERE DirectorCNP = 5100;

-- b)
-- Write a query on table Tb with a WHERE clause of the form WHERE b2 = value and analyze its execution plan. 
-- Create a nonclustered index that can speed up the query. Examine the execution plan again.
SELECT *
FROM Writers
WHERE WriterNumberOfWritings = 100;

GO
IF EXISTS (SELECT name FROM sys.indexes WHERE name = N'IX_Writers_WriterNumberOfWritings')
	DROP INDEX IX_Writers_WriterNumberOfWritings ON Writers

GO
CREATE NONCLUSTERED INDEX IX_Writers_WriterNumberOfWritings ON Writers(WriterNumberOfWritings)

-- cost before creating index: 0.01618
-- cost after creating index:  0.00329


-- c)
-- Create a view that joins at least 2 tables. Check whether existing indexes are helpful;
-- if not, reassess existing indexes / examine the cardinality of the tables.
GO
CREATE OR ALTER VIEW View1 AS
	SELECT M.MovieID, D.DirectorID, W.WriterID
	FROM Movies M
	INNER JOIN Directors D ON D.DirectorID = M.DirectorID
	INNER JOIN Writers W ON W.WriterID = M.WriterID
	WHERE W.WriterNumberOfWritings > 200 AND D.DirectorNumberOfDirections < 150

GO
SELECT * 
FROM View1; 

/*
	with existing indexes (automatically created ones + nonclustered index on b2 in Tb): 0.20087
	when adding a nonclustered index on a3 in Ta: 0.18861
	when deleting the nonclustered on a3 and b2: 0.21158
	automatically created ones + nonclustered on b2 + nonclustered on a3 + nonclustered on (aid, bid) in Tc: 0.18787
*/

IF EXISTS (SELECT name FROM sys.indexes WHERE name = N'IX_Directors_DirectorNumberOfDirections')
	DROP INDEX IX_Directors_DirectorNumberOfDirections ON Directors

CREATE NONCLUSTERED INDEX IX_Directors_DirectorNumberOfDirections ON Directors(DirectorNumberOfDirections)

IF EXISTS (SELECT name FROM sys.indexes WHERE name = N'IX_Movies_aidbid')
	DROP INDEX IX_Movies_aidbid ON Movies

CREATE NONCLUSTERED INDEX IX_Movies_aidbid ON Movies(DirectorID, WriterID)