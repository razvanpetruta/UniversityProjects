use StreamingMovies;
go


create or alter function validateMovieTitle (@title varchar(50)) 
returns int 
as
begin
	declare @return int = 0

	if (@title is not null and len(@title) >= 1) begin
		set @return=1
	end

	return @return
end
go


create or alter function validateMovieReleaseDate (@date date)
returns int
as
begin
    declare @return int = 0

    if (@date is not null and @date < GETDATE()) begin
        set @return = 1
	end

    return @return
end
go


create or alter function validateMovieRating (@rating decimal(10, 2))
returns int
as
begin
    declare @return int = 0

    if (@rating is not null and @rating >= 0 and @rating <= 10) begin
        set @return = 1
	end

    return @return
end
go


create or alter function validateActorName (@name nvarchar(50))
returns int
as
begin
    declare @return int = 0

    if (@name is not null and len(@name) >= 5) begin
        set @return = 1
	end

    return @return
end
go


create or alter function validateActorBirthday (@birthday date)
returns int
as
begin
    declare @return int = 0

    if (@birthday is not null and @birthday < GETDATE()) begin
        set @return = 1
	end

    return @return
end
go


create or alter function validateActorIncome (@income float)
returns int
as
begin
    declare @return int = 0

    if (@income is not null and @income >= 0) begin
        set @return = 1
	end

    return @return
end
go


create or alter procedure addMovieActor @movieTitle nvarchar(50), 
										@movieReleaseDate date, 
										@movieRating decimal(10, 2),
										@actorName nvarchar(50),
										@actorBirthday date,
										@actorIncome float
as
begin
	begin tran
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

		if (dbo.validateActorName(@actorName) <> 1) begin
			raiserror('The actor name must not be empty and must be at least 5 characters', 14, 1)
		end

		if (dbo.validateActorBirthday(@actorBirthday) <> 1) begin
			raiserror('The actor birhtday must not be empty and must be in the past', 14, 1)
		end

		if (dbo.validateActorIncome(@actorIncome) <> 1) begin
			raiserror('The actor income must not be empty and must be positive', 14, 1)
		end

		insert into Movies values (@movieTitle, @movieReleaseDate, @movieRating)
		declare @movieID int = scope_identity()

		insert into Actors values (@actorName, @actorBirthday, @actorIncome)
		declare @actorID int = scope_identity()

		insert into Acts (MovieID, ActorID) values (@movieID, @actorID)

		commit tran
		select 'Transaction commited'

	end try

	begin catch
		rollback tran
		select 'Transaction rollbacked: ' + ERROR_MESSAGE()
	end catch
end



-- DOES WORK
select * from Movies
select * from Actors
select * from Acts

exec addMovieActor @movieTitle = 'Bladerunner 2049', 
				   @movieReleaseDate = '2020-10-10', 
				   @movieRating = 9.1,
				   @actorName = 'Ryan Gosling',
				   @actorBirthday = '1980-06-05',
				   @actorIncome = 1000000;

select * from Movies
select * from Actors
select * from Acts



-- DOES NOT WORK
select * from Movies
select * from Actors
select * from Acts

exec addMovieActor @movieTitle = 'Bladerunner 2049', 
				   @movieReleaseDate = '2024-10-10', 
				   @movieRating = 9.1,
				   @actorName = 'Ryan Gosling',
				   @actorBirthday = '1980-06-05',
				   @actorIncome = 1000000;

select * from Movies
select * from Actors
select * from Acts