#pragma once
#include "Service.h"

typedef struct
{
	Service* serv;
} UI;


/*
	Function for allocating space for Service instance.
*/
UI* createUI(Service* serv);


/*
	Function for deallocating space for Service instance.
*/
void destroyUI(UI* ui);


/*
	Function for running the application.
*/
void startUI(UI* ui);