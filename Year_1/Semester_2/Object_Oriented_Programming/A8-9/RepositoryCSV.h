#pragma once
#include "Repository.h"

class RepositoryCSV : public Repository
{
	public:
		RepositoryCSV() : Repository{} {};

		~RepositoryCSV() {};

		void saveAdoptionListAndOpen() override;
};