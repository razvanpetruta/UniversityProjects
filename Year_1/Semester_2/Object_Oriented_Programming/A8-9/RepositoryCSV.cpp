#include "RepositoryCSV.h"
#include <fstream>
#include "Exceptions.h"

void RepositoryCSV::saveAdoptionListAndOpen()
{
	std::ofstream f("RepositoryCSV.csv");

	if (!f.is_open())
		throw RepositoryException("Couldn't open the file to write\n");

	for (const auto& d : this->elements)
	{
		f << d.getId() << ',' << d.getBreed() << ',' << d.getName() << ',' << d.getAge() << ',' << d.getPhotograph() << '\n';
	}

	f.close();

	std::string s = "start RepositoryCSV.csv";
	system(s.c_str());
}
