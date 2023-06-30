package com.example.restapi.repository.library_repository;

import com.example.restapi.model.library.Library;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LibraryRepository extends JpaRepository<Library, Long>, CustomLibraryRepository {
    @NonNull
    Page<Library> findAll(@NonNull Pageable pageable);
    @Query(value = "SELECT * FROM libraries " +
            "WHERE to_tsvector('english', name) @@ to_tsquery('english', replace(?1, ' ', ':* & ') || ':*') " +
            "OR name LIKE ('%' || ?1 || '%') ORDER BY ts_rank(to_tsvector('english', name), " +
            "to_tsquery('english', replace(?1, ' ', ':* & ') || ':*')) DESC LIMIT 20", nativeQuery = true)
    List<Library> findTop20BySearchTerm(String searchTerm);
    Long countByUserID(Long userID);
}
