#pragma once

#include <QWidget>
#include "ui_UserWindow.h"
#include "Observer.h"
#include "Service.h"

class UserWindow : public QWidget, public Observer
{
	Q_OBJECT

public:
	UserWindow(User u, Service& service, QWidget *parent = Q_NULLPTR);
	~UserWindow();

	void update() override;

private:
	Ui::UserWindow ui;
	User u;
	Service& service;

	void connectSignalsAndSlots();

	void populateList();

	void addHandle();

	void addAnswerHandle();

	void handleSelected();

	void voteHandle();

	void voteSpinnerHandle();
};
