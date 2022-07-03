#include "Utils.h"

vector<string> tokenize(string line, char delimiter)
{
	vector<string> result;
	stringstream ss{ line };
	string token;
	while (getline(ss, token, delimiter))
	{
		result.push_back(token);
	}
	return result;
}
