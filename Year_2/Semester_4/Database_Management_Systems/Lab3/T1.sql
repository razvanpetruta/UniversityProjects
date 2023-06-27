--Dirty Reeds
BEGIN TRANSACTION
	UPDATE Movies SET MovieTitle='Dirty Read'
	WHERE MovieID = 1
	WAITFOR DELAY '00:00:10'
ROLLBACK TRANSACTION

--NON-REPEATABLE READS
INSERT INTO Movies(MovieTitle, MovieReleaseDate, MovieRating) VALUES ('Title example', '2022-10-10', 7)
BEGIN TRAN
	WAITFOR DELAY '00:00:05'
	UPDATE Movies SET MovieRating=10 WHERE MovieTitle = 'Title example'
COMMIT TRAN


--PHANTOM READS
BEGIN TRAN
	WAITFOR DELAY '00:00:04'
	INSERT INTO Movies(MovieTitle, MovieReleaseDate, MovieRating) VALUES ('Title example', '2022-10-10', 7)
COMMIT TRAN

--DEADLOCK
SET DEADLOCK_PRIORITY HIGH
BEGIN TRAN
UPDATE Movies SET MovieTitle = 'Deadlock 1' WHERE MovieID = 1
-- this transaction has exclusively lock on table Movies
WAITFOR DELAY '00:00:10'
UPDATE Actors SET ActorName = 'Deadlock 1' WHERE ActorID = 2
-- this transaction will be blocked because transaction 2 has already blocked our lock on table Actors
COMMIT TRAN

