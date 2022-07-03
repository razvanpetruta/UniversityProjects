#pragma once
#include <QAbstractTableModel>
#include "Service.h"

class IdeasModel : public QAbstractTableModel
{
private:
	Service& service;

public:
	IdeasModel(Service& service);

	int rowCount(const QModelIndex& parent = QModelIndex()) const override;

	int columnCount(const QModelIndex& parent = QModelIndex()) const override;

	QVariant data(const QModelIndex& index, int role = Qt::DisplayRole) const override;

	QVariant headerData(int section, Qt::Orientation orientation, int role = Qt::DisplayRole) const override;

	Qt::ItemFlags flags(const QModelIndex& index) const override;

	bool setData(const QModelIndex& index, const QVariant& value, int role = Qt::EditRole) override;

	void addIdea(std::string description, std::string status, std::string creator, int act);

	void reviseIdea(int index);

	std::vector<Idea> getIdeaas();
};