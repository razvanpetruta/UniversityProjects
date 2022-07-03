#pragma once
#include <QTableView>
#include "Repository.h"
#include "Service.h"
#include <QtWidgets/qlabel.h>
#include <QtWidgets/qpushbutton.h>
#include <QtWidgets/qlistwidget.h>
#include <QRadioButton>
#include <QFormLayout>
#include <QtWidgets/qapplication.h>
#include <QShortcut>

class DogTableModel : public QAbstractTableModel 
{
	private:
		Repository* repository;

	public:
		explicit DogTableModel(Repository* repository);

		int rowCount(const QModelIndex& parent = QModelIndex()) const;

		int columnCount(const QModelIndex& parent = QModelIndex()) const;

		QVariant data(const QModelIndex& index, int role = Qt::DisplayRole) const;

		QVariant headerData(int section, Qt::Orientation orientation, int role = Qt::DisplayRole) const;

		void update();
};

class GUI : public QWidget
{
	Q_OBJECT

	private:
		Service* service;

		void initGUI();

		QLabel* titleWidget;
		QPushButton* adminButton;
		QPushButton* userButton;

		void showAdmin();
		void showUser();
		void connectSignalsAndSlots();

	public:
		explicit GUI(Service* service);
		~GUI() override;
};

class AdminGUI : public QWidget
{
	Q_OBJECT

	private:
		Service* service;

		void initAdminGUI();

		QLabel* titleWidget;
		QListWidget* dogsListWidget;
		QLineEdit* nameLineEdit, * breedLineEdit, * ageLineEdit, * linkLineEdit, * idLineEdit;
		QPushButton* addButton, * deleteButton, * updateButton;
		QPushButton* undoButton, * redoButton;
		QShortcut* undoShortcut, * redoShortcut;

		void populateList();
		void connectSignalsAndSlots();
		int getSelectedIndex() const;
		void addDog();
		void deleteDog();
		void updateDog();
		void undoGUI();
		void redoGUI();

	public:
		explicit AdminGUI(QWidget* parent, Service* service);
		~AdminGUI() override;
};

class UserGUI : public QWidget
{
	Q_OBJECT

	private:
		Service* service;

		void initUserGUI();

		QLabel* titleWidget;
		QListWidget* dogsListWidget, * filteredDogs, * filteredByName;
		QLineEdit* idLineEdit, * nameLineEdit, * breedLineEdit, * ageLineEdit, * linkLineEdit, * breedFilterLineEdit, * ageFilterLineEdit;
		QLineEdit* nameFilterLineEdit;
		QPushButton* addButton, * filterButton, * openListButton, * showAdoptionList;
		QRadioButton* csvButton, * htmlButton;
		DogTableModel* adoptionListTableModel;
		QTableView* adoptionListTable;

		bool repoTypeSelected, filtered;
		void populateDogList();
		void populateAdoptionList();
		void connectSignalsAndSlots();
		int getSelectedIndex() const;
		void addDog();
		void filterDogs();
		void filterDogsByName();
		void createTable();
		void showAdoption();

	public:
		explicit UserGUI(QWidget* parent, Service* service);
		~UserGUI() override;
};