package com.example.restapi.repository.book_repository;

import com.example.restapi.model.book.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    @NonNull
    Page<Book> findAll(@NonNull Pageable pageable);
    Page<Book> findByPriceGreaterThanEqual(Double price, Pageable pageable);
    List<Book> findByLibraryID(Long id);
    @NonNull
    @EntityGraph(attributePaths = "library")
    Optional<Book> findById(@NonNull Long id);
    Long countByLibraryID(Long libraryID);
    Long countByUserID(Long userID);
    Long countByPriceGreaterThanEqual(Double price);
}
