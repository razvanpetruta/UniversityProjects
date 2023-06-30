package com.example.restapi.repository.library_repository;

import com.example.restapi.dtos.librarydtos.LibrariesCountDTO;
import com.example.restapi.model.book.Book;
import com.example.restapi.model.library.Library;
import com.example.restapi.model.membership.Membership;
import com.example.restapi.model.reader.Reader;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;

import java.util.List;

public class CustomLibraryRepositoryImpl implements CustomLibraryRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<LibrariesCountDTO> findLibrariesGroupByCountBooksDesc() {
        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<LibrariesCountDTO> query = cb.createQuery(LibrariesCountDTO.class);
        Root<Library> libraryRoot = query.from(Library.class);
        Join<Library, Book> libraryBookJoin = libraryRoot.join("books", JoinType.INNER);

        query.select(cb.construct(
                LibrariesCountDTO.class,
                libraryRoot.get("ID"),
                libraryRoot.get("name"),
                cb.count(libraryBookJoin.get("title"))
        ))
                .groupBy(libraryRoot.get("ID"), libraryRoot.get("name"))
                .orderBy(cb.desc(cb.count(libraryBookJoin.get("title"))));

        return this.entityManager.createQuery(query).setMaxResults(50).getResultList();
    }

    @Override
    public List<LibrariesCountDTO> findLibrariesGroupByCountStudentReadersDesc() {
        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<LibrariesCountDTO> query = cb.createQuery(LibrariesCountDTO.class);
        Root<Library> libraryRoot = query.from(Library.class);
        Join<Library, Membership> libraryMembershipJoin = libraryRoot.join("memberships", JoinType.INNER);
        Join<Membership, Reader> membershipBookJoin = libraryMembershipJoin.join("reader", JoinType.INNER);

        query.select(cb.construct(
                LibrariesCountDTO.class,
                libraryRoot.get("ID"),
                libraryRoot.get("name"),
                cb.count(membershipBookJoin.get("isStudent"))
        ))
                .where(cb.isTrue(membershipBookJoin.get("isStudent")))
                .groupBy(libraryRoot.get("ID"), libraryRoot.get("name"))
                .orderBy(cb.desc(cb.count(membershipBookJoin.get("isStudent"))));

        return this.entityManager.createQuery(query).setMaxResults(50).getResultList();
    }
}
