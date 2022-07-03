#include "FinalUI.h"
#include <QtWidgets/QApplication>

int main(int argc, char* argv[])
{
    QApplication a(argc, argv);
    Repository* repo = new Repository{};
    Repository* adoptionRepo = new Repository{};
    repo->initialiseRepositoryFromFile();
    Service* serv = new Service{ repo, adoptionRepo };
    GUI gui{ serv };
    gui.show();
    return a.exec();
}
