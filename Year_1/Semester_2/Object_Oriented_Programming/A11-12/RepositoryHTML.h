#pragma once
#include "Repository.h"

class RepositoryHTML : public Repository
{
	public:
		RepositoryHTML() : Repository{} {};

		~RepositoryHTML() {};

		void saveAdoptionListAndOpen() override;
};