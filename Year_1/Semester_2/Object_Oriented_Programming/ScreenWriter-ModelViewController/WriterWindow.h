#pragma once

#include <QWidget>
#include "ui_WriterWindow.h"
#include "IdeasTableModel.h"
#include <QTextEdit>

class WriterWindow : public QWidget
{
	Q_OBJECT

public:
	WriterWindow(ScreenWriter writer, IdeasModel* model, QWidget *parent = Q_NULLPTR);
	~WriterWindow();

private:
	Ui::WriterWindow ui;
	ScreenWriter writer;
	IdeasModel* model;

	std::vector<QTextEdit*> textEdits;
	std::vector<QPushButton*> saveButtons;

	int getSelectedIndex();

	void handleDevelopButton();

public slots:
	void addHandle();

	void reviseHandle();

	void handleDevelop();
};
