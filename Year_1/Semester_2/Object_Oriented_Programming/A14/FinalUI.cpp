#include "FinalUI.h"
#include <QVBoxLayout>
#include <QLineEdit>
#include <QFormLayout>
#include <QHeaderView>
#include <iostream>
#include "Exceptions.h"
#include <qmessagebox.h>

void GUI::initGUI()
{
	auto* layout = new QVBoxLayout{ this };
	QFont titleFont = this->titleWidget->font();
	this->titleWidget->setText("<p style='text-align:center'><font color=#00008B>---------------- Dog Shelter App! ----------------<br> Connect as:</font></p>");
	titleFont.setItalic(true);
	titleFont.setPointSize(12);
	titleFont.setStyleHint(QFont::System);
	titleFont.setWeight(QFont::Weight(70));
	this->titleWidget->setFont(titleFont);
	layout->addWidget(this->titleWidget);
	this->adminButton->setText("Admin");
	layout->addWidget(this->adminButton);
	this->userButton->setText("User");
	layout->addWidget(this->userButton);
	this->setLayout(layout);
}

void GUI::showAdmin()
{
	auto* admin = new AdminGUI(this, this->service);
	admin->show();
}

void GUI::showUser()
{
	auto* user = new UserGUI(this, this->service);
	user->show();
}

void GUI::connectSignalsAndSlots()
{
	QObject::connect(this->adminButton, &QPushButton::clicked, this, &GUI::showAdmin);
	QObject::connect(this->userButton, &QPushButton::clicked, this, &GUI::showUser);
}

GUI::GUI(Service* service) : service{ service }
{
	this->titleWidget = new QLabel(this);
	this->adminButton = new QPushButton(this);
	this->userButton = new QPushButton(this);
	this->initGUI();
	this->connectSignalsAndSlots();
}

GUI::~GUI() = default;

void AdminGUI::initAdminGUI()
{
	auto* layout = new QVBoxLayout(this);
	QFont titleFont = this->titleWidget->font();
	this->titleWidget->setText("<p style='text-align:center'><font color=#00008B>ADMIN MODE</font></p>");
	titleFont.setItalic(true);
	titleFont.setPointSize(12);
	titleFont.setStyleHint(QFont::System);
	titleFont.setWeight(QFont::Weight(70));
	this->titleWidget->setFont(titleFont);
	layout->addWidget(this->titleWidget);

	layout->addWidget(this->dogsListWidget);

	auto* dogDetailsLayout = new QFormLayout{};
	dogDetailsLayout->addRow("Id", this->idLineEdit);
	dogDetailsLayout->addRow("Breed", this->breedLineEdit);
	dogDetailsLayout->addRow("Name", this->nameLineEdit);
	dogDetailsLayout->addRow("Age", this->ageLineEdit);
	dogDetailsLayout->addRow("Link", this->linkLineEdit);
	layout->addLayout(dogDetailsLayout);

	auto* buttonsLayout = new QGridLayout{};
	buttonsLayout->addWidget(this->addButton, 0, 0);
	buttonsLayout->addWidget(this->deleteButton, 0, 1);
	buttonsLayout->addWidget(this->updateButton, 1, 0, 1, 2);
	buttonsLayout->addWidget(this->undoButton, 2, 0);
	buttonsLayout->addWidget(this->redoButton, 2, 1);
	layout->addLayout(buttonsLayout);
}

void AdminGUI::populateList()
{
	this->dogsListWidget->clear();
	std::vector<Dog> allDogs = this->service->getDogsRepoElements();
	for (Dog& dog : allDogs)
		this->dogsListWidget->addItem(QString::fromStdString(dog.toString()));
}

void AdminGUI::connectSignalsAndSlots()
{
	QObject::connect(this->dogsListWidget, &QListWidget::itemSelectionChanged, [this]()
		{
			int selectedIndex = this->getSelectedIndex();
			if (selectedIndex < 0)
				return;
			Dog dog = this->service->getDogsRepoElements()[selectedIndex];
			this->idLineEdit->setText(QString::fromStdString(std::to_string(dog.getId())));
			this->nameLineEdit->setText(QString::fromStdString(dog.getName()));
			this->breedLineEdit->setText(QString::fromStdString(dog.getBreed()));
			this->ageLineEdit->setText(QString::fromStdString(std::to_string(dog.getAge())));
			this->linkLineEdit->setText(QString::fromStdString(dog.getPhotograph()));
		});

	QObject::connect(this->addButton, &QPushButton::clicked, this, &AdminGUI::addDog);
	QObject::connect(this->deleteButton, &QPushButton::clicked, this, &AdminGUI::deleteDog);
	QObject::connect(this->updateButton, &QPushButton::clicked, this, &AdminGUI::updateDog);
	QObject::connect(this->undoButton, &QPushButton::clicked, this, &AdminGUI::undoGUI);
	QObject::connect(this->redoButton, &QPushButton::clicked, this, &AdminGUI::redoGUI);
	QObject::connect(this->undoShortcut, &QShortcut::activated, this, &AdminGUI::undoGUI);
	QObject::connect(this->redoShortcut, &QShortcut::activated, this, &AdminGUI::redoGUI);
}

int AdminGUI::getSelectedIndex() const
{
	QModelIndexList selectedIndexes = this->dogsListWidget->selectionModel()->selectedIndexes();
	if (selectedIndexes.empty()) {
		this->idLineEdit->clear();
		this->nameLineEdit->clear();
		this->breedLineEdit->clear();
		this->ageLineEdit->clear();
		this->linkLineEdit->clear();
		return -1;
	}
	int selectedIndex = selectedIndexes.at(0).row();
	return selectedIndex;
}

void AdminGUI::addDog()
{
	std::string idS = this->idLineEdit->text().toStdString();
	std::string breed = this->breedLineEdit->text().toStdString();
	std::string name = this->nameLineEdit->text().toStdString();
	std::string ageS = this->ageLineEdit->text().toStdString();
	std::string link = this->linkLineEdit->text().toStdString();
	try
	{
		int age, id;
		age = stoi(ageS);
		id = stoi(idS);
		this->service->addDog(id, breed, name, age, link);
		this->populateList();
		this->idLineEdit->clear();
		this->nameLineEdit->clear();
		this->breedLineEdit->clear();
		this->ageLineEdit->clear();
		this->linkLineEdit->clear();
	}
	catch (DogException& de)
	{
		auto* error = new QMessageBox();
		error->setIcon(QMessageBox::Critical);
		error->setText(de.what());
		error->setWindowTitle("Invalid input!");
		error->exec();
	}
	catch (RepositoryException& re)
	{
		auto* error = new QMessageBox();
		error->setIcon(QMessageBox::Critical);
		error->setText(re.what());
		error->setWindowTitle("Duplicate id!");
		error->exec();
	}
	catch (...)
	{
		auto* error = new QMessageBox();
		error->setIcon(QMessageBox::Critical);
		error->setText("Please fill in the fields!\n");
		error->setWindowTitle("Empty fields!");
		error->exec();
	}
}

void AdminGUI::deleteDog()
{
	std::string idS = this->idLineEdit->text().toStdString();
	try
	{
		int id = stoi(idS);
		this->service->removeDog(id);
		this->populateList();
		this->idLineEdit->clear();
		this->nameLineEdit->clear();
		this->breedLineEdit->clear();
		this->ageLineEdit->clear();
		this->linkLineEdit->clear();
	}
	catch (RepositoryException& re)
	{
		auto* error = new QMessageBox();
		error->setIcon(QMessageBox::Critical);
		error->setText(re.what());
		error->setWindowTitle("Id not found!");
		error->exec();
	}
	catch (...)
	{
		auto* error = new QMessageBox();
		error->setIcon(QMessageBox::Critical);
		error->setText("Please fill in the id!\n");
		error->setWindowTitle("Empty fields!");
		error->exec();
	}
}

void AdminGUI::updateDog()
{
	int index = this->getSelectedIndex();
	try
	{
		if (index >= 0)
		{
			int id = this->service->getDogsRepoElements()[index].getId();
			std::string newName = this->nameLineEdit->text().toStdString();
			std::string newBreed = this->breedLineEdit->text().toStdString();
			std::string ageS = this->ageLineEdit->text().toStdString();
			std::string newLink = this->breedLineEdit->text().toStdString();
			if (newName != "")
				this->service->updateName(id, newName);
			if (newBreed != "")
				this->service->updateBreed(id, newBreed);
			if (ageS != "")
			{
				int age;
				age = stoi(ageS);
				this->service->updateAge(id, age);
			}
			if (newLink != "")
				this->service->updatePhotograph(id, newLink);
			this->populateList();
		}
		else
		{
			auto* error = new QMessageBox();
			error->setIcon(QMessageBox::Critical);
			error->setText("No dog selected!");
			error->setWindowTitle("Selection error!");
			error->exec();
		}
	}
	catch (RepositoryException& re)
	{
		auto* error = new QMessageBox();
		error->setIcon(QMessageBox::Critical);
		error->setText(re.what());
		error->setWindowTitle("Duplicate id!");
		error->exec();
	}
}

void AdminGUI::undoGUI()
{
	try
	{
		this->service->undoLastAction();
		this->populateList();
	}
	catch (RepositoryException& re)
	{
		auto* error = new QMessageBox();
		error->setIcon(QMessageBox::Critical);
		error->setText(re.what());
		error->setWindowTitle("Error!");
		error->exec();
	}
}

void AdminGUI::redoGUI()
{
	try
	{
		this->service->redoLastAction();
		this->populateList();
	}
	catch (RepositoryException& re)
	{
		auto* error = new QMessageBox();
		error->setIcon(QMessageBox::Critical);
		error->setText(re.what());
		error->setWindowTitle("Error!");
		error->exec();
	}
}

AdminGUI::AdminGUI(QWidget* parent, Service* service) : service{ service }
{
	this->titleWidget = new QLabel(this);
	this->dogsListWidget = new QListWidget{};
	this->idLineEdit = new QLineEdit{};
	this->nameLineEdit = new QLineEdit{};
	this->breedLineEdit = new QLineEdit{};
	this->ageLineEdit = new QLineEdit{};
	this->linkLineEdit = new QLineEdit{};
	this->addButton = new QPushButton("Add");
	this->deleteButton = new QPushButton("Delete");
	this->updateButton = new QPushButton("Update");
	this->undoButton = new QPushButton("Undo");
	this->redoButton = new QPushButton("Redo");
	this->undoShortcut = new QShortcut(QKeySequence(Qt::CTRL + Qt::Key_Z), this);
	this->redoShortcut = new QShortcut(QKeySequence(Qt::CTRL + Qt::Key_Y), this);
	setParent(parent);
	setWindowFlag(Qt::Window);
	this->initAdminGUI();
	this->populateList();
	this->connectSignalsAndSlots();
}

AdminGUI::~AdminGUI() = default;

void UserGUI::initUserGUI()
{
	auto* layout = new QVBoxLayout{ this };
	QFont titleFont = this->titleWidget->font();
	this->titleWidget->setText("<p style='text-align:center'><font color=#00008B>USER MODE <br> Select the type of file you want for saving your adopted dogs!</font></p>");
	titleFont.setItalic(true);
	titleFont.setPointSize(12);
	titleFont.setStyleHint(QFont::System);
	titleFont.setWeight(QFont::Weight(70));
	this->titleWidget->setFont(titleFont);
	layout->addWidget(this->titleWidget);

	auto* radioButtonsLayout = new QGridLayout{};
	radioButtonsLayout->addWidget(this->csvButton, 0, 0);
	radioButtonsLayout->addWidget(this->htmlButton, 1, 0);
	radioButtonsLayout->addWidget(this->openListButton, 0, 1, 2, 1);
	layout->addLayout(radioButtonsLayout);

	layout->addWidget(this->dogsListWidget);

	auto* adoptionListButtonLayout = new QVBoxLayout{};
	adoptionListButtonLayout->addWidget(this->showAdoptionList);
	layout->addLayout(adoptionListButtonLayout);

	auto* dogDetailsLayout = new QFormLayout{};
	this->idLineEdit->setEnabled(false);
	dogDetailsLayout->addRow("Id", this->idLineEdit);
	this->breedLineEdit->setEnabled(false);
	dogDetailsLayout->addRow("Breed", this->breedLineEdit);
	this->nameLineEdit->setEnabled(false);
	dogDetailsLayout->addRow("Name", this->nameLineEdit);
	this->ageLineEdit->setEnabled(false);
	dogDetailsLayout->addRow("Age", this->ageLineEdit);
	this->linkLineEdit->setEnabled(false);
	dogDetailsLayout->addRow("Link", this->linkLineEdit);
	dogDetailsLayout->addRow(this->addButton);
	layout->addLayout(dogDetailsLayout);

	auto* filterTitle = new QLabel("<p style='text-align:center'><font color=#00008B><br>Filter the available dogs by breed and age</font></p>");
	QFont filterFont = filterTitle->font();
	filterFont.setPointSize(10);
	filterFont.setStyleHint(QFont::System);
	filterFont.setWeight(QFont::Weight(63));
	filterTitle->setFont(filterFont);
	layout->addWidget(filterTitle);

	auto* filterDetailsLayout = new QFormLayout{};
	filterDetailsLayout->addRow("Breed", this->breedFilterLineEdit);
	filterDetailsLayout->addRow("Age", this->ageFilterLineEdit);
	filterDetailsLayout->addRow(this->filterButton);
	layout->addLayout(filterDetailsLayout);

	layout->addWidget(this->filteredDogs);

	auto* filterName = new QLabel("<p style='text-align:center'><font color=#00008B><br>Filter the available dogs by name</font></p>");
	QFont filterFontName = filterName->font();
	filterFontName.setPointSize(10);
	filterFontName.setStyleHint(QFont::System);
	filterFontName.setWeight(QFont::Weight(63));
	filterName->setFont(filterFont);
	layout->addWidget(filterName);

	auto* filterByNameDetailsLayout = new QFormLayout{};
	filterByNameDetailsLayout->addRow("Name", this->nameFilterLineEdit);
	layout->addLayout(filterByNameDetailsLayout);

	layout->addWidget(this->filteredByName);
}

void UserGUI::populateDogList()
{
	this->dogsListWidget->clear();
	std::vector<Dog> allDogs = this->service->getDogsRepoElements();
	for (Dog& dog : allDogs)
		this->dogsListWidget->addItem(QString::fromStdString(std::to_string(dog.getId()) + " " + dog.getName()));
}

void UserGUI::populateAdoptionList()
{
	this->adoptionListTableModel->update();
}

void UserGUI::connectSignalsAndSlots()
{
	QObject::connect(this->dogsListWidget, &QListWidget::itemClicked, [this]()
		{
			std::string field = this->dogsListWidget->selectedItems().at(0)->text().toStdString();
			std::string idS = "";
			for (auto l : field)
			{
				if (l != ' ')
					idS += l;
				else
					break;
			}
			int index = this->service->getIdIndex(stoi(idS));
			Dog dog = this->service->getDogsRepoElements()[index];
			this->idLineEdit->setText(QString::fromStdString(std::to_string(dog.getId())));
			this->nameLineEdit->setText(QString::fromStdString(dog.getName()));
			this->breedLineEdit->setText(QString::fromStdString(dog.getBreed()));
			this->ageLineEdit->setText(QString::fromStdString(std::to_string(dog.getAge())));
			this->linkLineEdit->setText(QString::fromStdString(dog.getPhotograph()));
			//std::string link = std::string("start ").append(dog.getPhotograph());
			//system(link.c_str());
		});

	QObject::connect(this->csvButton, &QRadioButton::clicked, [this]()
		{
			this->service->changeUserAdoptionListType("csv");
			this->createTable();
			this->repoTypeSelected = true;
		});

	QObject::connect(this->htmlButton, &QRadioButton::clicked, [this]()
		{
			this->service->changeUserAdoptionListType("html");
			this->createTable();
			this->repoTypeSelected = true;
		});

	QObject::connect(this->openListButton, &QPushButton::clicked, [this]() {
		if (this->repoTypeSelected)
		{
			this->service->saveAdoptionList();
		}
		});

	QObject::connect(this->addButton, &QPushButton::clicked, this, &UserGUI::addDog);
	QObject::connect(this->filterButton, &QPushButton::clicked, this, &UserGUI::filterDogs);
	QObject::connect(this->nameFilterLineEdit, &QLineEdit::textChanged, this, &UserGUI::filterDogsByName);
	QObject::connect(this->showAdoptionList, &QPushButton::clicked, this, &UserGUI::showAdoption);
}

int UserGUI::getSelectedIndex() const
{
	QModelIndexList selectedIndexes = this->dogsListWidget->selectionModel()->selectedIndexes();
	if (selectedIndexes.empty()) {
		this->idLineEdit->clear();
		this->nameLineEdit->clear();
		this->breedLineEdit->clear();
		this->ageLineEdit->clear();
		this->linkLineEdit->clear();
		return -1;
	}
	int selectedIndex = selectedIndexes.at(0).row();
	return selectedIndex;
}

void UserGUI::addDog()
{
	if (this->repoTypeSelected)
	{
		try
		{
			std::string idS = this->idLineEdit->text().toStdString();
			if (!idS.empty())
			{
				int id;
				id = stoi(idS);
				this->service->adoptDog(id);
				if (!this->filtered)
				{
					this->populateDogList();
					this->populateAdoptionList();
					this->idLineEdit->clear();
					this->nameLineEdit->clear();
					this->breedLineEdit->clear();
					this->ageLineEdit->clear();
					this->linkLineEdit->clear();
				}
			}
		}
		catch (DogException& de)
		{
			auto* error = new QMessageBox();
			error->setIcon(QMessageBox::Critical);
			error->setText(de.what());
			error->setWindowTitle("Invalid input!");
			error->exec();
		}
		catch (RepositoryException& re)
		{
			auto* error = new QMessageBox();
			error->setIcon(QMessageBox::Critical);
			error->setText(re.what());
			error->setWindowTitle("Duplicate id!");
			error->exec();
		}
		catch (...)
		{
			auto* error = new QMessageBox();
			error->setIcon(QMessageBox::Critical);
			error->setText("Please fill in the fields!\n");
			error->setWindowTitle("Empty fields!");
			error->exec();
		}
	}
	else
	{
		auto* error = new QMessageBox();
		error->setIcon(QMessageBox::Warning);
		error->setText("Please select the type of file you want!");
		error->setWindowTitle("File type warning!");
		error->exec();
	}
}

void UserGUI::filterDogs()
{
	std::string breedFilter = this->breedFilterLineEdit->text().toStdString();
	std::string ageFilterS = this->ageFilterLineEdit->text().toStdString();
	int age;

	if (!ageFilterS.empty())
	{
		age = stoi(ageFilterS);
		std::vector<Dog> elements = this->service->getDogsRepoElements();
		std::vector<Dog> matches;
		copy_if(elements.begin(), elements.end(), std::back_inserter(matches),
			[breedFilter, age](const Dog& dog)
			{
				return (breedFilter == "" || dog.getBreed().find(breedFilter) != std::string::npos) && dog.getAge() <= age;
			});
		this->filteredDogs->clear();
		for (Dog& dog : matches)
			this->filteredDogs->addItem(QString::fromStdString(dog.toString()));
	}
}

void UserGUI::filterDogsByName()
{
	std::string nameFilter = this->nameFilterLineEdit->text().toStdString();

	if (!nameFilter.empty())
	{
		std::vector<Dog> elements = this->service->getDogsRepoElements();
		std::vector<Dog> matches;
		copy_if(elements.begin(), elements.end(), std::back_inserter(matches),
			[nameFilter](const Dog& dog)
			{
				return dog.getName().find(nameFilter) != std::string::npos;
			});
		this->filteredByName->clear();
		for (Dog& dog : matches)
			this->filteredByName->addItem(QString::fromStdString(dog.toString()));
	}
	else
	{
		this->filteredByName->clear();
	}
}

void UserGUI::createTable()
{
	this->adoptionListTableModel = new DogTableModel{ this->service->getUserRepo() };
	this->adoptionListTable = new QTableView{};
	this->adoptionListTable->setModel(this->adoptionListTableModel);
	this->adoptionListTable->horizontalHeader()->setSectionResizeMode(QHeaderView::Stretch);
}

void UserGUI::showAdoption()
{
	if (this->repoTypeSelected)
	{
		this->adoptionListTable->show();
	}
	else
	{
		auto* error = new QMessageBox();
		error->setIcon(QMessageBox::Critical);
		error->setText("Please select the type of repo");
		error->setWindowTitle("Invalid repo!");
		error->exec();
	}
}

UserGUI::UserGUI(QWidget* parent, Service* service) : service{ service }
{
	this->titleWidget = new QLabel(this);
	this->dogsListWidget = new QListWidget{};
	this->filteredDogs = new QListWidget{};
	this->filteredByName = new QListWidget{};
	this->idLineEdit = new QLineEdit{};
	this->nameLineEdit = new QLineEdit{};
	this->breedLineEdit = new QLineEdit{};
	this->ageLineEdit = new QLineEdit{};
	this->linkLineEdit = new QLineEdit{};
	this->breedFilterLineEdit = new QLineEdit{};
	this->ageFilterLineEdit = new QLineEdit{};
	this->nameFilterLineEdit = new QLineEdit{};
	this->addButton = new QPushButton("Add to the adoption list");
	this->filterButton = new QPushButton("Filter");
	this->openListButton = new QPushButton("Open file");
	this->csvButton = new QRadioButton("CSV");
	this->htmlButton = new QRadioButton("HTML");
	this->showAdoptionList = new QPushButton("Show adoption list");
	this->repoTypeSelected = false;
	this->filtered = false;
	setParent(parent);
	setWindowFlag(Qt::Window);
	this->initUserGUI();
	this->populateDogList();
	this->connectSignalsAndSlots();
}

UserGUI::~UserGUI() = default;

DogTableModel::DogTableModel(Repository* repository)
{
	this->repository = repository;
}

int DogTableModel::rowCount(const QModelIndex& parent) const
{
	return this->repository->getSize();
}

int DogTableModel::columnCount(const QModelIndex& parent) const
{
	return 5;
}

QVariant DogTableModel::data(const QModelIndex& index, int role) const
{
	int row = index.row();
	Dog currentDog = this->repository->getElements()[row];
	int column = index.column();
	if (role == Qt::DisplayRole || role == Qt::EditRole)
	{
		switch (column)
		{
		case 0:
			return QString::fromStdString(std::to_string(currentDog.getId()));
		case 1:
			return QString::fromStdString(currentDog.getBreed());
		case 2:
			return QString::fromStdString(currentDog.getName());
		case 3:
			return QString::fromStdString(std::to_string(currentDog.getAge()));
		case 4:
			return QString::fromStdString(currentDog.getPhotograph());
		default:
			break;
		}
	}
	return QVariant();
}

QVariant DogTableModel::headerData(int section, Qt::Orientation orientation, int role) const
{
	if (role == Qt::DisplayRole)
	{
		if (orientation == Qt::Horizontal)
		{
			switch (section)
			{
			case 0:
				return QString("Id");
			case 1:
				return QString("Breed");
			case 2:
				return QString("Name");
			case 3:
				return QString("Age");
			case 4:
				return QString("Link");
			default:
				break;
			}
		}
	}
	return QVariant();
}

void DogTableModel::update()
{
	QModelIndex topLeft = this->index(0, 0);
	QModelIndex bottomRight = this->index(this->rowCount(), this->columnCount());
	emit layoutChanged();
	emit dataChanged(topLeft, bottomRight);
}
