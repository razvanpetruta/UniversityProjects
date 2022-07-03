#pragma once
#include <string>

class Dog
{
	private:
		int id;
		std::string breed;
		std::string name;
		int age;
		std::string photograph;

	public:
		// default constructor
		Dog();

		// parameterized constructor
		Dog(int _id, const std::string& _breed, const std::string& _name, int _age, const std::string& _photograph);

		// getter function for id
		int getId() const;

		// getter function for breed
		std::string getBreed() const;

		// getter function for name
		std::string getName() const;

		// getter function for age
		int getAge() const;

		// getter function for photograph
		std::string getPhotograph() const;

		// setter function for breed
		void setBreed(std::string _breed);

		// setter function for name
		void setName(std::string _name);

		// setter function for age
		void setAge(int _age);

		// setter function for photograph
		void setPhotograph(std::string _photograph);

		// return the string representation of a Dog instance
		std::string toString() const;
};
