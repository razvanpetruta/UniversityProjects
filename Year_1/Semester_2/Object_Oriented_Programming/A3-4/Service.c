#include "Service.h"
#include <stdlib.h>
#include <assert.h>
#include <string.h>

Service* createService(Repository* repo)
{
    Service* s = (Service*)malloc(sizeof(Service));

    if (s == NULL)
        return NULL;

    s->repo = repo;

    return s;
}

void destroyService(Service* s)
{
    if (s == NULL)
        return;

    destroyRepository(s->repo);

    free(s);
}

int addEstate(Service* serv, char* type, char* address, int price, int surface)
{
    if (serv == NULL)
        return 0;

    if (type == NULL || address == NULL)
        return 0;

    Estate* e = createEstate(type, address, price, surface);

    if (e == NULL)
        return 0;

    int added = addToRepository(serv->repo, e);
    if (added == 0)
        destroyEstate(e);

    return added;
}

Repository* getRepository(Service* s)
{
    if (s == NULL)
        return NULL;

    return s->repo;
}

int deleteEstate(Service* serv, char* address)
{
    if (serv == NULL || address == NULL)
        return 0;

    int deleted = deleteFromRepository(serv->repo, address);
    
    return deleted;
}

int updateEstate(Service* serv, char* address, char* newType, int newPrice, int newSurface)
{
    if (serv == NULL || address == NULL)
        return 0;

    int updated = updateFromRepository(serv->repo, address, newType, newPrice, newSurface);

    return updated;
}

void populateRepo(Service* serv)
{
    addEstate(serv, "house", "100A", 100, 64);
    addEstate(serv, "apartment", "101B", 200, 128);
    addEstate(serv, "penthouse", "102C", 300, 98);
    addEstate(serv, "house", "103A", 150, 51);
    addEstate(serv, "apartment", "104E", 250, 152);
    addEstate(serv, "penthouse", "105A", 350, 111);
    addEstate(serv, "house", "126G", 120, 78);
    addEstate(serv, "apartment", "127H", 270, 228);
    addEstate(serv, "penthouse", "128I", 310, 198);
    addEstate(serv, "house", "129J", 170, 251);
}

int filterByAddress(Service* serv, char* filter, Vector* result)
{
    if (serv == NULL)
        return 0;

    int i;
    if (filter[0] == '\0')
    {
        for (i = 0; i < getLength(serv->repo); i++)
        {
            Estate* e = createEstate(serv->repo->v->data[i]->type, serv->repo->v->data[i]->address, serv->repo->v->data[i]->price, serv->repo->v->data[i]->surface);
            addToVector(result, e);
        }
    }
    else
        for (i = 0; i < getLength(serv->repo); i++)
        {
            char* found;
            found = strstr(serv->repo->v->data[i]->address, filter);
            if (found != NULL)
            {
                Estate* e = createEstate(serv->repo->v->data[i]->type, serv->repo->v->data[i]->address, serv->repo->v->data[i]->price, serv->repo->v->data[i]->surface);
                addToVector(result, e);
            }
        }

    return 1;
}

int searchEstateByTypeAndSurface(Service* serv, char* type, int surface, Vector* result)
{
    if (serv == NULL)
        return 0;

    int i;
    for (i = 0; i < getLength(serv->repo); i++)
    {
        if (strcmp(serv->repo->v->data[i]->type, type) == 0 && serv->repo->v->data[i]->surface >= surface)
        {
            Estate* e = createEstate(serv->repo->v->data[i]->type, serv->repo->v->data[i]->address, serv->repo->v->data[i]->price, serv->repo->v->data[i]->surface);
            addToVector(result, e);
        }
    }
    
    return 1;
}

int searchEstateByPrice(Service* serv, int price, int(*filter)(Estate* e, int price), Vector* result)
{
    if (serv == NULL)
        return 0;

    int i;
    for (i = 0; i < getLength(serv->repo); i++)
    {
        if ((*filter)(serv->repo->v->data[i], price) == 1)
        {
            Estate* e = createEstate(serv->repo->v->data[i]->type, serv->repo->v->data[i]->address, serv->repo->v->data[i]->price, serv->repo->v->data[i]->surface);
            addToVector(result, e);
        }
    }

    return 1;
}

// TESTS
void testAddEstate()
{
    Repository* repo = createRepository(10);
    Service* serv = createService(repo);

    assert(getLength(repo) == 0);

    assert(addEstate(serv, "house", "100A", 100, 64));
    assert(getLength(repo) == 1);

    assert(addEstate(serv, "penthouse", "100A", 88, 76) == 0);

    destroyService(serv);
}

void testDeleteEstate()
{
    Repository* repo = createRepository(10);
    Service* serv = createService(repo);

    assert(getLength(repo) == 0);

    assert(addEstate(serv, "house", "100A", 100, 64));
    assert(getLength(repo) == 1);

    assert(deleteEstate(serv, "100A") == 1);
    assert(getLength(repo) == 0);

    assert(deleteEstate(serv, "100A") == 0);

    destroyService(serv);
}

void testUpdateEstate()
{
    Repository* repo = createRepository(10);
    Service* serv = createService(repo);

    assert(getLength(repo) == 0);

    assert(addEstate(serv, "house", "100A", 100, 64));
    assert(getLength(repo) == 1);

    assert(updateEstate(serv, "100A", "penthouse", 1000, 200) == 1);
    assert(updateEstate(serv, "100B", "apartment", 200, 75) == 0);

    destroyService(serv);
}

void testService()
{
    testAddEstate();
    testDeleteEstate();
    testUpdateEstate();
}
