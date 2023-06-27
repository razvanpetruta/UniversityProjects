use StreamingMovies;
go

create or alter procedure addMovieActorASAP @movieTitle nvarchar(50), 
										@movieReleaseDate date, 
										@movieRating decimal(10, 2),
										@actorName nvarchar(50),
										@actorBirthday date,
										@actorIncome float
as
begin
	-- insert movie transaction
	begin tran InsertMovie
	begin try
		if (dbo.validateMovieTitle(@movieTitle) <> 1) begin
			raiserror('The movie title must not be empty and must be at least 1 character', 14, 1)
		end

		if (dbo.validateMovieReleaseDate(@movieReleaseDate) <> 1) begin
			raiserror('The movie release date must not be empty and must be in the past', 14, 1)
		end

		if (dbo.validateMovieRating(@movieRating) <> 1) begin
			raiserror('The movie rating must not be empty and must be between 0 and 10', 14, 1)
		end

		insert into Movies values (@movieTitle, @movieReleaseDate, @movieRating)
		declare @movieID int = scope_identity()

		commit tran InsertMovie
		select 'Insert Movie Transaction commited'
	end try

	begin catch
		rollback tran
		select 'Insert Movie Transaction rollbacked: ' + ERROR_MESSAGE()
		return 1
	end catch

	-- insert actor transaction
	begin tran InsertActor
	begin try
		if (dbo.validateActorName(@actorName) <> 1) begin
			raiserror('The actor name must not be empty and must be at least 5 characters', 14, 1)
		end

		if (dbo.validateActorBirthday(@actorBirthday) <> 1) begin
			raiserror('The actor birhtday must not be empty and must be in the past', 14, 1)
		end

		if (dbo.validateActorIncome(@actorIncome) <> 1) begin
			raiserror('The actor income must not be empty and must be positive', 14, 1)
		end

		insert into Actors values (@actorName, @actorBirthday, @actorIncome)
		declare @actorID int = scope_identity()

		commit tran InsertActor
		select 'Insert Actor Transaction commited'
	end try

	begin catch
		rollback tran
		select 'Insert Actor Transaction rollbacked: ' + ERROR_MESSAGE()
		return 1
	end catch

	-- associate movie and actor
	begin tran InsertActs
	begin try
		insert into Acts (MovieID, ActorID) values (@movieID, @actorID)

		commit tran InsertActs
		select 'Insert Acts Transaction commited'
	end try

	begin catch
		rollback tran
		select 'Insert Acts Transaction rollbacked: ' + ERROR_MESSAGE()
		return 1
	end catch
end



select * from Movies
select * from Actors
select * from Acts

exec addMovieActorASAP @movieTitle = 'One life', 
				   @movieReleaseDate = '2022-10-10', 
				   @movieRating = 9.1,
				   @actorName = 'Ryan Gosling',
				   @actorBirthday = '1980-06-05',
				   @actorIncome = -1;

select * from Movies
select * from Actors
select * from Acts