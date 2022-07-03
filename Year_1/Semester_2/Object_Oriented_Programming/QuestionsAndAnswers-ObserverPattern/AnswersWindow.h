#pragma once

#include <QWidget>
#include "ui_AnswersWindow.h"
#include "Service.h"

class AnswersWindow : public QWidget
{
	Q_OBJECT

public:
	AnswersWindow(Service& service, QWidget *parent = Q_NULLPTR);
	~AnswersWindow();

private:
	Ui::AnswersWindow ui;
	Service& service;

	void searchHandle();
};
