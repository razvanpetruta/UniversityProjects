package com.example.restapi.service.reader_service;

import com.example.restapi.dtos.readerdtos.ReaderDTO_forAll;
import com.example.restapi.dtos.readerdtos.ReaderDTO_forOne;
import com.example.restapi.model.reader.Reader;

import java.util.List;

public interface IReaderService {
    List<ReaderDTO_forAll> getAllReaders(Integer pageNo, Integer pageSize);

    Reader addNewReader(Reader newReader, Long userID);

    ReaderDTO_forOne getReaderById(Long id);

    Reader replaceReader(Reader newReader, Long readerID, Long userID);

    void deleteReader(Long id, Long userID);

    long countAllReaders();
}
