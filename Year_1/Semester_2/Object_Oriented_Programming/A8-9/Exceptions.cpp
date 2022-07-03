#include "Exceptions.h"

// DOG
const char* DogException::what() const noexcept
{
	return this->message.c_str();
}

void DogValidator::validate(const Dog& d)
{
	std::string errors = "";

	if (d.getBreed().size() == 0)
		errors += "The breed cannot be empty\n";

	if (d.getName().size() == 0)
		errors += "The name cannot be empty\n";

	if (d.getAge() < 0)
		errors += "The age cannot be a negative number\n";

	if (d.getPhotograph().size() == 0)
		errors += "Please provide a link to the dog's photo\n";

	if (errors.size() > 0)
		throw DogException(errors);
}

// REPOSITORY
const char* RepositoryException::what() const noexcept
{
	return this->message.c_str();
}
