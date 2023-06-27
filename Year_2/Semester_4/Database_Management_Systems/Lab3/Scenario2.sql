SET TRANSACTION ISOLATION LEVEL SNAPSHOT
BEGIN TRAN
	Select * from Movies where MovieID = 1
	Waitfor delay '00:00:10'
	select * from Movies where MovieID = 1
	UPDATE Movies SET MovieTitle = 'Some title' WHERE MovieID = 1
	-- process will block
	-- Process will receive Error 3960.
COMMIT TRAN