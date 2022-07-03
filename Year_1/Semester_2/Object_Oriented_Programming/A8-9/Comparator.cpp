#include "Comparator.h"

bool ComparatorAscendingByName::compare(Dog first, Dog second)
{
    return first.getName() <= second.getName();
}

bool ComparatorAscendingByAge::compare(Dog first, Dog second)
{
    return first.getAge() <= second.getAge();
}
