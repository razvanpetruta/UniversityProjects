#include "Tests.h"
#include "Dog.h"
#include <cassert>
#include "Repository.h"
#include "Service.h"
#include <iostream>

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

	std::string s = "Bulldog";

	d1.setBreed(s);
	assert(d1.getBreed() == "Bulldog");

	s = "Spike";
	d1.setName(s);
	assert(d1.getName() == "Spike");

	s = "link";
	d1.setPhotograph(s);
	assert(d1.getPhotograph() == "link");

	Dog d2{ 1, "Beagle", "Spike", 10, "link" };

	assert(d2.getId() == 1);
	assert(d2.getBreed() == "Beagle");
	assert(d2.getName() == "Spike");
	assert(d2.getAge() == 10);
	assert(d2.getPhotograph() == "link");

	assert(d2.toString() == "id: 1, breed: Beagle, name: Spike, age: 10, photograph:\n\tlink");
}

//void Tests::testRepository()
//{
//	Repository repo;
//
//	assert(repo.getSize() == 0);
//	
//	Dog d{ 1, "Beagle", "Spike", 10, "link" };
//	repo.add(d);
//	assert(repo.add(d) == false);
//	assert(repo.getSize() == 1);
//
//	assert(repo.getIdIndex(1) == 0);
//
//	std::string newBreed = "Bulldog", newName = "Ronaldo", newPhotograph = "newLink";
//
//	bool updated;
//	updated = repo.updateBreed(1, newBreed);
//	assert(updated == true);
//	updated = repo.updateBreed(2, newBreed);
//	assert(updated == false);
//
//	updated = repo.updateName(1, newName);
//	assert(updated == true);
//	updated = repo.updateName(2, newName);
//	assert(updated == false);
//
//	updated = repo.updatePhotograph(1, newPhotograph);
//	assert(updated == true);
//	updated = repo.updatePhotograph(2, newPhotograph);
//	assert(updated == false);
//
//	updated = repo.updateAge(1, 5);
//	assert(updated == true);
//	updated = repo.updateAge(2, 5);
//	assert(updated == false);
//
//	std::vector<Dog> v = repo.getElements();
//	assert(v.size() == 1);
//
//	assert(v[0].getBreed() == "Bulldog");
//
//	repo.remove(1);
//	assert(repo.remove(1) == false);
//	assert(repo.getSize() == 0);
//}

//void Tests::testService()
//{
//	Repository dogsRepo, userAdoptionList;
//	Service serv{ dogsRepo, userAdoptionList };
//
//	serv.addDog(1, "Beagle", "Spike", 10, "link");
//	assert(serv.getDogsRepoSize() == 1);
//	assert(serv.getUserAdoptionListSize() == 0);
//
//	std::string s = "new";
//
//	assert(serv.updateBreed(1, s) == true);
//	assert(serv.updateName(1, s) == true);
//	assert(serv.updatePhotograph(1, s) == true);
//	assert(serv.updateAge(1, 11) == true);
//
//	s += '1';
//	assert(serv.getDogsRepoElements()[0].getBreed() == "new");
//	
//	assert(serv.removeDog(1) == true);
//	assert(serv.removeDog(1) == false);
//
//	serv.addDog(2, "Beagle", "Spike", 10, "link");
//
//	serv.adoptDog(2);
//	assert(serv.getDogsRepoSize() == 0);
//	assert(serv.getUserAdoptionListSize() == 1);
//
//	assert(serv.getUserAdpotionElements()[0].getBreed() == "Beagle");
//
//	serv.addDog(2, "Beagle", "Spike", 10, "link");
//	assert(serv.removeDog(2) == true);
//	assert(serv.removeDog(2) == false);
//
//	serv.addDog(2, "Beagle", "Spike", 10, "link");
//
//	std::string filter = "B";
//	assert(serv.adoptDog(1) == false);
//}

void Tests::testSort()
{
	Dog d1{ 1, "Beagle", "Boyka", 11, "link" };
	Dog d2{ 2, "Beagle", "Arnold", 1, "link" };
	Dog d3{ 3, "Beagle", "Ana", 10, "link" };
	Dog d4{ 4, "Beagle", "Balto", 8, "link" };
	std::vector<Dog> v{ d1, d2, d3, d4 };

	Comparator<Dog>* c1 = new ComparatorAscendingByName{};
	Comparator<Dog>* c2 = new ComparatorAscendingByAge{};

	customSort(v, c1);

	assert(v[0].getName() == "Ana");
	assert(v[1].getName() == "Arnold");
	assert(v[3].getName() == "Boyka");

	customSort(v, c2);

	assert(v[0].getAge() == 1);
	assert(v[1].getAge() == 8);
	assert(v[3].getAge() == 11);

	delete c1;
	delete c2;
}

void Tests::testAll()
{
	Tests::testDog();
	//Tests::testRepository();
	//Tests::testService();
	Tests::testSort();
}
