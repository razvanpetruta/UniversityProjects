package com.example.restapi.repository.reader_repository;

import com.example.restapi.model.reader.Reader;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReaderRepository extends JpaRepository<Reader, Long> {
    @NonNull
    Page<Reader> findAll(@NonNull Pageable pageable);
    List<Reader> findReadersByMembershipsLibraryID(Long libraryID);
    Long countByUserID(Long userID);
}
