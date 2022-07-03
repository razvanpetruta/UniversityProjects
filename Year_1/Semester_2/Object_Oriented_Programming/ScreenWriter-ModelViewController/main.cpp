#include "ScreenWriterProject.h"
#include <QtWidgets/QApplication>
#include "Repository.h"
#include "Service.h"
#include "WriterWindow.h"

using namespace std;

int main(int argc, char *argv[])
{
    QApplication a(argc, argv);
    Repository repo{};
    Service service{ repo };
    vector<WriterWindow*> windows;
    IdeasModel* model = new IdeasModel{ service };

    for (auto el : service.getWriters())
    {
        auto window = new WriterWindow{ el, model };
        windows.push_back(window);
        window->show();
    }
    
    return a.exec();
}
