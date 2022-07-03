#include "Tests.h"
#include "Dog.h"
#include <cassert>
#include "DynamicVector.h"
#include "Repository.h"
#include "Service.h"

void Tests::testDog()
{
	Dog d1;

	assert(d1.getId() == 0);
	assert(d1.getBreed() == "");
	assert(d1.getName() == "");
	assert(d1.getAge() == 0);
	assert(d1.getPhotograph() == "");

	d1.setAge(10);
	assert(d1.getAge() == 10);

	d1.setBreed("Bulldog");
	assert(d1.getBreed() == "Bulldog");

	d1.setName("Spike");
	assert(d1.getName() == "Spike");

	d1.setPhotograph("link");
	assert(d1.getPhotograph() == "link");

	Dog d2{ 1, "Beagle", "Spike", 10, "link" };

	assert(d2.getId() == 1);
	assert(d2.getBreed() == "Beagle");
	assert(d2.getName() == "Spike");
	assert(d2.getAge() == 10);
	assert(d2.getPhotograph() == "link");

	assert(d2.toString() == "id: 1, breed: Beagle, name: Spike, age: 10, photograph: link");
}

void Tests::testDynamicVector()
{
	DynamicVector<int> v;

	assert(v.getSize() == 0);

	v.add(1);
	v.add(2);
	v.add(3);
	v.add(4);
	v.add(5);
	v.add(6);
	v.add(7);
	v.add(8);
	v.add(1);
	v.add(2);
	v.add(3);
	v.add(3);
	v.add(3);

	assert(v.getSize() == 13);
	
	assert(v[0] == 1);
	assert(v[1] == 2);
	
	v.remove(1);
	assert(v[1] == 3);

	{
		DynamicVector<int> v1{ 3 };
		v1.add(1);
		v1.add(2);
		v1.add(3);

		DynamicVector<int> v2{ v1 };
		assert(v2.getSize() == 3);

		v = v1;
		v = v;
	}

	assert(v.getSize() == 3);
}

void Tests::testRepository()
{
	Repository repo;

	assert(repo.getSize() == 0);
	
	Dog d{ 1, "Beagle", "Spike", 10, "link" };
	repo.add(d);
	assert(repo.add(d) == false);
	assert(repo.getSize() == 1);

	assert(repo.getIdIndex(1) == 0);

	std::string newBreed = "Bulldog", newName = "Ronaldo", newPhotograph = "newLink";

	bool updated;
	updated = repo.updateBreed(1, newBreed);
	assert(updated == true);
	updated = repo.updateBreed(2, newBreed);
	assert(updated == false);

	updated = repo.updateName(1, newName);
	assert(updated == true);
	updated = repo.updateName(2, newName);
	assert(updated == false);

	updated = repo.updatePhotograph(1, newPhotograph);
	assert(updated == true);
	updated = repo.updatePhotograph(2, newPhotograph);
	assert(updated == false);

	updated = repo.updateAge(1, 5);
	assert(updated == true);
	updated = repo.updateAge(2, 5);
	assert(updated == false);

	DynamicVector<Dog> v = repo.getElements();
	assert(v.getSize() == 1);

	assert(v[0].getBreed() == "Bulldog");

	repo.remove(1);
	assert(repo.remove(1) == false);
	assert(repo.getSize() == 0);
}

void Tests::testService()
{
	Repository dogsRepo, userAdoptionList;
	Service serv{ dogsRepo, userAdoptionList };

	serv.addDog(1, "Beagle", "Spike", 10, "link");
	assert(serv.getDogsRepoSize() == 1);
	assert(serv.getUserAdoptionListSize() == 0);

	std::string s = "new";

	assert(serv.updateBreed(1, s) == true);
	assert(serv.updateName(1, s) == true);
	assert(serv.updatePhotograph(1, s) == true);
	assert(serv.updateAge(1, 11) == true);

	s += '1';
	assert(serv.getDogsRepoElements()[0].getBreed() == "new");
	
	assert(serv.removeDog(1) == true);
	assert(serv.removeDog(1) == false);

	serv.addDog(2, "Beagle", "Spike", 10, "link");

	serv.adoptDog(2);
	assert(serv.getDogsRepoSize() == 0);
	assert(serv.getUserAdoptionListSize() == 1);

	assert(serv.getUserAdpotionElements()[0].getBreed() == "Beagle");

	serv.addDog(2, "Beagle", "Spike", 10, "link");
	assert(serv.removeDog(2) == true);
	assert(serv.removeDog(2) == false);

	serv.addDog(2, "Beagle", "Spike", 10, "link");

	std::string filter = "B";
	assert(serv.filterDogsByBreedAndAge(filter, 100)[0].getName() == "Spike");
	assert(serv.adoptDog(1) == false);
}

void Tests::testAll()
{
	Tests::testDog();
	Tests::testDynamicVector();
	Tests::testRepository();
	Tests::testService();
}
