--Dirty Reeds
SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED
BEGIN TRAN
	SELECT * FROM Movies
	WAITFOR DELAY '00:00:15'
	SELECT * FROM Movies
COMMIT TRAN


--Dirty Reeds Solution
SET TRANSACTION ISOLATION LEVEL READ COMMITTED
BEGIN TRAN
	SELECT * FROM Movies
	WAITFOR DELAY '00:00:15'
	SELECT * FROM Movies
COMMIT TRAN

--NON-REPEATABLE READS
SET TRANSACTION ISOLATION LEVEL READ COMMITTED
BEGIN TRAN
	SELECT * FROM Movies
	WAITFOR DELAY '00:00:05'
	SELECT * FROM Movies
COMMIT TRAN

--NON-REPEATABLE READS Solution
SET TRANSACTION ISOLATION LEVEL REPEATABLE READ
BEGIN TRAN
	SELECT * FROM Movies
	WAITFOR DELAY '00:00:05'
	SELECT * FROM Movies
COMMIT TRAN

--PHANTOM READS
SET TRANSACTION ISOLATION LEVEL REPEATABLE READ
BEGIN TRAN
	SELECT * FROM Movies
	WAITFOR DELAY '00:00:05'
	SELECT * FROM Movies
COMMIT TRAN

--PHANTOM READS Solution
SET TRANSACTION ISOLATION LEVEL SERIALIZABLE
BEGIN TRAN
	SELECT * FROM Movies
	WAITFOR DELAY '00:00:05'
	SELECT * FROM Movies
COMMIT TRAN

--DEADLOCK
BEGIN TRAN
UPDATE Actors SET ActorName = 'Deadlock 2' WHERE ActorID = 2
-- this transaction has exclusively lock on table Actors
WAITFOR DELAY '00:00:10'
UPDATE Movies SET MovieTitle = 'Deadlock 2' WHERE MovieID = 1
-- this transaction will be blocked because transaction 1 has exclusively lock on table Movies, so, both of the transactions are blocked
COMMIT TRAN

--DEADLOCK Solution
SET DEADLOCK_PRIORITY HIGH
BEGIN TRAN
UPDATE Actors SET ActorName = 'Deadlock 2' WHERE ActorID = 2
-- this transaction has exclusively lock on table Actors
WAITFOR DELAY '00:00:10'
UPDATE Movies SET MovieTitle = 'Deadlock 2' WHERE MovieID = 1
-- this transaction will be blocked because transaction 1 has exclusively lock on table Movies, so, both of the transactions are blocked
COMMIT TRAN

select * from Movies
select * from Actors
