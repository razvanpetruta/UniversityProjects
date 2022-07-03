#include "QuestionsAndAnswers.h"
#include <QtWidgets/QApplication>
#include "UserWindow.h"
#include "AnswersWindow.h"

using namespace std;

int main(int argc, char *argv[])
{
    QApplication a(argc, argv);
    
    Repository repo{};
    Service service{ repo };
    vector<UserWindow*> windows;

    AnswersWindow w{ service };
    w.show();

    for (auto u : service.getUsers())
    {
        auto window = new UserWindow{ u, service };
        windows.push_back(window);
        window->show();
    }

    return a.exec();
}
