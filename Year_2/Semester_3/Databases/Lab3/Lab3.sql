USE StreamingMovies;

-- modify the type of a column
GO
CREATE OR ALTER PROCEDURE setMovieRatingFromMoviesDecimal AS
	ALTER TABLE Movies ALTER COLUMN MovieRating DECIMAL(10, 2)


GO
CREATE OR ALTER PROCEDURE setMovieRatingFromMoviesFloat AS
	ALTER TABLE Movies ALTER COLUMN MovieRating FLOAT


-- add / remove a column
GO
CREATE OR ALTER PROCEDURE addActorCountryToActors AS
	ALTER TABLE Actors ADD ActorCountry VARCHAR(50)

GO
CREATE OR ALTER PROCEDURE removeActorCountryFromActors AS
	ALTER TABLE Actors DROP COLUMN ActorCountry


-- add / remove DEFAULT constraint
GO
CREATE OR ALTER PROCEDURE addDefaultToUserWatchHoursFromUsers AS
	ALTER TABLE Users ADD CONSTRAINT UserWatchHoursDefault DEFAULT(0) FOR UserWatchHours

GO
CREATE OR ALTER PROCEDURE removeDefaultFromUserWatchHoursFromUsers AS
	ALTER TABLE Users DROP CONSTRAINT UserWatchHoursDefault


-- create / drop a table
GO
CREATE OR ALTER PROCEDURE createAdminsTable AS
	CREATE TABLE Admins (
		AdminID INT NOT NULL,
		AdminName VARCHAR(50) NOT NULL,
		AdminBirthday DATE,
		MovieID INT NOT NULL,
		CONSTRAINT AdminPrimaryKey PRIMARY KEY(AdminID)
	)

GO
CREATE OR ALTER PROCEDURE dropAdminsTable AS
	DROP TABLE Admins


-- add / remove primary key
GO
CREATE OR ALTER PROCEDURE addIdAndNamePrimaryKeyToAdmins AS
	ALTER TABLE Admins DROP AdminPrimaryKey
	ALTER TABLE Admins ADD CONSTRAINT AdminPrimaryKey PRIMARY KEY(AdminID, AdminName)

GO
CREATE OR ALTER PROCEDURE removeIdAndNamePrimaryKeyToAdmins AS
	ALTER TABLE Admins DROP AdminPrimaryKey
	ALTER TABLE Admins ADD CONSTRAINT AdminPrimaryKey PRIMARY KEY(AdminID)


-- add / remove candidate key
GO
CREATE OR ALTER PROCEDURE addCandidateKeyActors AS
	ALTER TABLE Actors ADD CONSTRAINT ActorsNewCandidateKey UNIQUE(ActorName, ActorBirthday, ActorIncome)

GO
CREATE OR ALTER PROCEDURE removeCandidateKeyActors AS
	ALTER TABLE Actors DROP CONSTRAINT ActorsNewCandidateKey


-- add / remove foreign key
GO
CREATE OR ALTER PROCEDURE addForeignKeyAdmins AS
	ALTER TABLE Admins ADD CONSTRAINT AdminForeignKey FOREIGN KEY(MovieID) REFERENCES Movies(MovieID)

GO
CREATE OR ALTER PROCEDURE removeForeignKeyAdmins AS
	ALTER TABLE Admins DROP CONSTRAINT AdminForeignKey


-- table that contains the current version of the database
CREATE TABLE VersionTable (
	CurrentVersion INT
);

INSERT INTO VersionTable VALUES (1); -- initial version

DROP TABLE VersionTable;


-- a table that contains the initial version, the version after the execution of the procedure
-- and the procedure name
CREATE TABLE ProcedureTable (
	InitialVersion INT NOT NULL,
	FinalVersion INT NOT NULL,
	ProcedureName VARCHAR(150) NOT NULL,
	PRIMARY KEY (InitialVersion, FinalVersion)
)

INSERT INTO ProcedureTable VALUES
	(1, 2, 'setMovieRatingFromMoviesDecimal'),
	(2, 1, 'setMovieRatingFromMoviesFloat'),
	(2, 3, 'addActorCountryToActors'),
	(3, 2, 'removeActorCountryFromActors'),
	(3, 4, 'addDefaultToUserWatchHoursFromUsers'),
	(4, 3, 'removeDefaultFromUserWatchHoursFromUsers'),
	(4, 5, 'createAdminsTable'),
	(5, 4, 'dropAdminsTable'),
	(5, 6, 'addIdAndNamePrimaryKeyToAdmins'),
	(6, 5, 'removeIdAndNamePrimaryKeyToAdmins'),
	(6, 7, 'addCandidateKeyActors'),
	(7, 6, 'removeCandidateKeyActors'),
	(7, 8, 'addForeignKeyAdmins'),
	(8, 7, 'removeForeignKeyAdmins');


-- procedure to bring the database to the specified version
GO
CREATE OR ALTER PROCEDURE goToVersion(@newVersion INT) AS
	DECLARE @currentVersion INT
	DECLARE @procedureName VARCHAR(150)
	SELECT @currentVersion = CurrentVersion FROM VersionTable

	IF (@newVersion > (SELECT MAX(FinalVersion) FROM ProcedureTable) OR @newVersion < 1)
		RAISERROR ('Invalid Version', 10, 1)
	ELSE BEGIN
		IF @newVersion = @currentVersion
			PRINT('The version already selected')
		ELSE BEGIN
			WHILE @currentVersion > @newVersion BEGIN
				SELECT @procedureName = ProcedureName FROM ProcedureTable WHERE InitialVersion = @currentVersion AND FinalVersion = @currentVersion - 1
				PRINT('Executing: ' + @procedureName)
				EXEC(@procedureName)
				SET @currentVersion = @currentVersion - 1
			END
			WHILE @currentVersion < @newVersion BEGIN
				SELECT @procedureName = ProcedureName FROM ProcedureTable WHERE InitialVersion = @currentVersion AND FinalVersion = @currentVersion + 1
				PRINT('Executing: ' + @procedureName)
				EXEC(@procedureName)
				SET @currentVersion = @currentVersion + 1
			END
			UPDATE VersionTable SET CurrentVersion = @newVersion
		END
	END

EXEC goToVersion 1;

SELECT * FROM VersionTable;

SELECT * FROM ProcedureTable;