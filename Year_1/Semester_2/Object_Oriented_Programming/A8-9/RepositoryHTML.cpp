#include "RepositoryHTML.h"
#include <fstream>
#include "Exceptions.h"
#include <sstream>

std::string getHTMLLine(const Dog& d)
{
	std::stringstream ss;

	ss << "\t" << "<tr>\n";
	ss << "\t\t" << "<td>" << d.getId() << "<td/>\n";
	ss << "\t\t" << "<td>" << d.getBreed() << "<td/>\n";
	ss << "\t\t" << "<td>" << d.getName() << "<td/>\n";
	ss << "\t\t" << "<td>" << d.getAge() << "<td/>\n";
	ss << "\t\t" << "<td>" << d.getPhotograph() << "<td/>\n";
	ss << "\t" << "<tr/>\n";

	return ss.str();
}

void RepositoryHTML::saveAdoptionListAndOpen()
{
	std::ofstream f("RepositoryHTML.html");

	if (!f.is_open())
		throw RepositoryException("Couldn't open the file to write\n");

	f << "<!DOCTYPE html>\n";
	f << "<html>\n";
	f << "<head>\n";
	f << "\t<title>Playlist</title>\n";
	f << "</head>\n";
	f << "<table border=\"1\">\n";

	f << "\t" << "<tr>\n";
	f << "\t\t" << "<td>ID<td/>\n";
	f << "\t\t" << "<td>BREED<td/>\n";
	f << "\t\t" << "<td>NAME<td/>\n";
	f << "\t\t" << "<td>AGE<td/>\n";
	f << "\t\t" << "<td>PHOTOGRAPH<td/>\n";
	f << "\t" << "<tr/>\n";

	for (const auto& d : this->elements)
	{
		f << getHTMLLine(d);
	}

	f << "</table>\n</body>\n</html>";

	f.close();

	std::string s = "start RepositoryHTML.html";
	system(s.c_str());
}
