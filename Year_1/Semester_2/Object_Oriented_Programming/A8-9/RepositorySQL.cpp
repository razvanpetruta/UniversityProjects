#include "RepositorySQL.h"
#include <sqlite3.h>
#include <sstream>
#include <iostream>

void RepositorySQL::insertDogSQL(const Dog& d)
{
	sqlite3* DB;

	int exit = sqlite3_open(this->DBpath, &DB);

	std::stringstream sql;
	sql << "INSERT INTO DOGS (ID, BREED, NAME, AGE, PHOTOGRAPH) VALUES (" << d.getId() << ", '" << d.getBreed() << "', '";
	sql << d.getName() << "', " << d.getAge() << ", '" << d.getPhotograph() << "');";

	exit = sqlite3_exec(DB, sql.str().c_str(), NULL, NULL, NULL);

	sqlite3_close(DB);
}

void RepositorySQL::deleteDogSQL(int id)
{
	sqlite3* DB;

	int exit = sqlite3_open(this->DBpath, &DB);

	std::string sql = "DELETE FROM DOGS WHERE ID = " + std::to_string(id) + ";";

	exit = sqlite3_exec(DB, sql.c_str(), NULL, NULL, NULL);

	sqlite3_close(DB);
}

void RepositorySQL::updateBreedSQL(int id, std::string& breed)
{
	sqlite3* DB;

	int exit = sqlite3_open(this->DBpath, &DB);

	std::string sql = "UPDATE DOGS SET BREED = '" + breed + "' WHERE ID = " + std::to_string(id) + ";";

	exit = sqlite3_exec(DB, sql.c_str(), NULL, NULL, NULL);

	sqlite3_close(DB);
}

void RepositorySQL::updateNameSQL(int id, std::string& name)
{
	sqlite3* DB;

	int exit = sqlite3_open(this->DBpath, &DB);

	std::string sql = "UPDATE DOGS SET NAME = '" + name + "' WHERE ID = " + std::to_string(id) + ";";

	exit = sqlite3_exec(DB, sql.c_str(), NULL, NULL, NULL);

	sqlite3_close(DB);
}

void RepositorySQL::updateAgeSQL(int id, int age)
{
	sqlite3* DB;

	int exit = sqlite3_open(this->DBpath, &DB);

	std::string sql = "UPDATE DOGS SET AGE = " + std::to_string(age) + " WHERE ID = " + std::to_string(id) + ";";

	exit = sqlite3_exec(DB, sql.c_str(), NULL, NULL, NULL);

	sqlite3_close(DB);
}

void RepositorySQL::updatePhotographSQL(int id, std::string& photograph)
{
	sqlite3* DB;

	int exit = sqlite3_open(this->DBpath, &DB);

	std::string sql = "UPDATE DOGS SET PHOTOGRAPH = '" + photograph + "' WHERE ID = " + std::to_string(id) + ";";

	exit = sqlite3_exec(DB, sql.c_str(), NULL, NULL, NULL);

	sqlite3_close(DB);
}

std::vector<Dog> dogs;

int callback(void* notUsed, int argc, char** argv, char** azColName)
{
	int id, age;
	std::string breed, name, photograph;

	id = std::stoi(argv[0]);
	breed = argv[1];
	name = argv[2];
	age = std::stoi(argv[3]);
	photograph = argv[4];

	Dog d{ id, breed, name, age, photograph };

	dogs.push_back(d);

	return 0;
}

void RepositorySQL::selectDogs()
{
	sqlite3* DB;

	int exit = sqlite3_open(this->DBpath, &DB);

	std::string sql = "SELECT * FROM DOGS;";

	exit = sqlite3_exec(DB, sql.c_str(), callback, NULL, NULL);

	for (const auto& d : dogs)
	{
		Repository::add(d);
	}

	sqlite3_close(DB);
}

RepositorySQL::RepositorySQL(): Repository{}
{
	sqlite3* DB;

	// create database
	sqlite3_open(this->DBpath, &DB);

	std::string sql = "CREATE TABLE IF NOT EXISTS DOGS("
		"ID INTEGER PRIMARY KEY, "
		"BREED TEXT NOT NULL, "
		"NAME TEXT NOT NULL, "
		"AGE INT NOT NULL, "
		"PHOTOGRAPH TEXT NOT NULL)";

	// create table
	sqlite3_exec(DB, sql.c_str(), NULL, NULL, NULL);

	sqlite3_close(DB);
}

void RepositorySQL::add(const Dog& d)
{
	Repository::add(d);

	this->insertDogSQL(d);
}

void RepositorySQL::remove(int id)
{
	Repository::remove(id);

	this->deleteDogSQL(id);
}

void RepositorySQL::updateBreed(int id, std::string& _breed)
{
	Repository::updateBreed(id, _breed);

	this->updateBreedSQL(id, _breed);
}

void RepositorySQL::updateName(int id, std::string& _name)
{
	Repository::updateName(id, _name);

	this->updateNameSQL(id, _name);
}

void RepositorySQL::updateAge(int id, int _age)
{
	Repository::updateAge(id, _age);

	this->updateAgeSQL(id, _age);
}

void RepositorySQL::updatePhotograph(int id, std::string& _photograph)
{
	Repository::updatePhotograph(id, _photograph);

	this->updatePhotographSQL(id, _photograph);
}

void RepositorySQL::initialiseRepositoryFromFile()
{
	this->selectDogs();
}