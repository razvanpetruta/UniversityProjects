class Nod:
    def __init__(self, e):
        self.e = e
        self.urm = None


class Lista:
    def __init__(self):
        self.prim = None


'''
crearea unei liste din valori citite pana la 0
'''


def creareLista():
    lista = Lista()
    lista.prim = creareLista_rec()
    return lista


def creareLista_rec():
    x = int(input("x="))
    if x == 0:
        return None
    else:
        nod = Nod(x)
        nod.urm = creareLista_rec()
        return nod


'''
tiparirea elementelor unei liste
'''


def tipar(lista):
    tipar_rec(lista.prim)


def tipar_rec(nod):
    if nod != None:
        print(nod.e)
        tipar_rec(nod.urm)


'''
program pentru test
'''


"""
4. a)
has_even_number_of_elements (l1, l2, l3, ..., ln) = {
    false, n == 1
    true, n == 0 // no element
    has_even_number_of_elements(l1, l2, l3, ..., ln - 2), otherwise
}
"""
def has_even_number_of_elements(head):
    if head is None:
        return True
    if head.urm is None:
        return False
    return has_even_number_of_elements(head.urm.urm)


"""
4. b)
delete_occurrence (l1, l2, l3, ..., ln, e) = {
    {}, n == 0
    {l1} + delete_ocurrence(l1, l2, l3, ..., ln - 1, e), if l1 != e
    delete_ocurrence(l2, l3, ..., ln, e), otherwise
}
"""
def delete_occurrence(my_list, current, prev, el):
    if current is None:
        return
    if current.e == el:
        if prev is None:
            my_list.prim = current.urm
            delete_occurrence(my_list, current.urm, None, el)
        else:
            prev.urm = current.urm
            delete_occurrence(my_list, current.urm, prev, el)
    else:
        delete_occurrence(my_list, current.urm, current, el)


def delete_first_occurrence(my_list, current, prev, el):
    if current is None:
        return
    if current.e == el:
        if prev is None:
            my_list.prim = current.urm
        else:
            prev.urm = current.urm
        return
    delete_occurrence(my_list, current.urm, current, el)


def main():
    list = creareLista()
    tipar(list)
    print(has_even_number_of_elements(list.prim))
    delete_occurrence(list, list.prim, None, 2)
    tipar(list)


main()
