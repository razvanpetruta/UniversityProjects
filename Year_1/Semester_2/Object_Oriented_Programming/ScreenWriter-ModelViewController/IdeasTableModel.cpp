#include "IdeasTableModel.h"
#include <exception>

using namespace std;

IdeasModel::IdeasModel(Service& service) : service{ service }
{
}

int IdeasModel::rowCount(const QModelIndex& parent) const
{
    return this->service.getIdeas().size();
}

int IdeasModel::columnCount(const QModelIndex& parent) const
{
    return 4;
}

QVariant IdeasModel::data(const QModelIndex& index, int role) const
{
	int row = index.row();
	int column = index.column();

	Idea i = this->service.getIdeas()[row];

	if (role == Qt::DisplayRole)
	{
		switch (column)
		{
		case 0:
			return QString::fromStdString(i.getDescription());

		case 1:
			return QString::fromStdString(i.getStatus());

		case 2:
			return QString::fromStdString(i.getCreator());

		case 3:
			return QString::number(i.getAct());

		default:
			break;
		}
	}

	return QVariant();
}

QVariant IdeasModel::headerData(int section, Qt::Orientation orientation, int role) const
{
	if (orientation == Qt::Horizontal)
	{
		if (role == Qt::DisplayRole)
		{
			switch (section)
			{
			case 0:
				return QString{ "Description" };

			case 1:
				return QString{ "Status" };

			case 2:
				return QString{ "Creator" };

			case 3:
				return QString{ "Act" };

			default:
				break;
			}
		}
	}

	return QVariant();
}

Qt::ItemFlags IdeasModel::flags(const QModelIndex& index) const
{
	if (index.column() == 3)
		return Qt::ItemIsEditable | Qt::ItemIsEnabled;

	if (index.column() == 0 || index.column() == 2)
		return Qt::ItemIsSelectable | Qt::ItemIsEnabled;

	return Qt::ItemFlags();
}

bool IdeasModel::setData(const QModelIndex& index, const QVariant& value, int role)
{
	int row = index.row();
	int column = index.column();

	Idea i = this->service.getIdeas()[row];

	if (role == Qt::EditRole || role == Qt::DisplayRole)
	{
		switch (column)
		{
		case 3:
			this->beginResetModel();
			this->service.setIdeaAct(i, value.toInt());
			this->endResetModel();
			break;
		default:
			break;
		}
	}

	return true;
}

void IdeasModel::addIdea(std::string description, std::string status, std::string creator, int act)
{
	if (this->service.isDescriptionPresent(description))
		throw exception{ "Description duplicate!" };

	if (act != 1 && act != 2 && act != 3)
		throw exception{ "Act must be 1, 2 or 3!" };

	this->beginResetModel();

	this->service.addIdea(description, status, creator, act);

	this->endResetModel();
}

void IdeasModel::reviseIdea(int index)
{
	if (this->service.getIdeas()[index].getStatus() != "proposed")
		throw exception{ "Status is not proposed!" };

	this->beginResetModel();

	this->service.reviseIdea(index);

	this->endResetModel();
}

std::vector<Idea> IdeasModel::getIdeaas()
{
	return this->service.getIdeas();
}
