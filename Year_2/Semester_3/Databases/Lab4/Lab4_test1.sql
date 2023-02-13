-- a table with a single-column primary key and no foreign keys
-- a view with a SELECT statement operating on one table

GO
CREATE OR ALTER VIEW actorsWithMoreThan3MilIncome AS
	SELECT A.ActorName, A.ActorIncome
	FROM Actors A
	WHERE A.ActorIncome > 3000000


GO
EXEC addToTables 'Actors'
EXEC addToViews 'actorsWithMoreThan3MilIncome'
EXEC addToTests 'test1'
EXEC connectTableToTest 'Actors', 'test1', 500, 1
EXEC connectViewToTest 'actorsWithMoreThan3MilIncome', 'test1'


GO
CREATE OR ALTER PROCEDURE populateTableActors (@numberOfRows INT) AS
	DECLARE @stringValue VARCHAR(50)
	WHILE @numberOfRows > 0 
	BEGIN
		EXEC generateRandomString @stringValue OUTPUT
		INSERT INTO Actors(ActorID, ActorName, ActorBirthday, ActorIncome) 
		VALUES (@numberOfRows, @stringValue, '2002-10-10', floor(rand() * 7) * 1000000)
		SET @numberOfRows = @numberOfRows - 1
	END
