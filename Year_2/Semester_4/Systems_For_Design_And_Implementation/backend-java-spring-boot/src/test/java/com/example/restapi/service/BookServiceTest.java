package com.example.restapi.service;

import com.example.restapi.repository.book_repository.BookRepository;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;
    @InjectMocks
    private com.example.restapi.service.book_service.IBookService IBookService;

//    @Before
//    public void init() {
//        when(this.bookRepository.findByPriceGreaterThan(10.0, PageRequest.of(0, 100))).thenReturn(Arrays.asList(
//                new Book(1L, "title1", "author1", "publisher1", 12.0, 2000, null),
//                new Book(2L, "title2", "author2", "publisher2", 14.0, 2000, null),
//                new Book(3L, "title3", "author3", "publisher3", 20.0, 2000, null),
//                new Book(4L, "title4", "author4", "publisher4", 40.0, 2000, null)
//        ));
//    }
//
//    @Test
//    public void testGetBooksWithPriceGreater() {
//        List<Book> result = this.bookService.getBooksWithPriceGreater(10.0);
//
//        assertEquals(4, result.size());
//        assertEquals("title1", result.get(0).getTitle());
//        assertEquals("title2", result.get(1).getTitle());
//        assertEquals("title3", result.get(2).getTitle());
//        assertEquals("title4", result.get(3).getTitle());
//    }
}
