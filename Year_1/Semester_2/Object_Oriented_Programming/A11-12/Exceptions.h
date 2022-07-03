#pragma once

#include <iostream>
#include "Dog.h"

// DOG EXCEPTION CLASS
class DogException : public std::exception
{
	private:
		std::string message;

	public:
		DogException(std::string message): message{ message } {};

		const char* what() const noexcept override;
};

// DOG VALIDATOR
class DogValidator
{
	public:
		static void validate(const Dog& d);
};


class RepositoryException : public std::exception
{
	private:
		std::string message;

	public:
		RepositoryException(std::string message): message{ message } {}

		const char* what() const noexcept override;
};