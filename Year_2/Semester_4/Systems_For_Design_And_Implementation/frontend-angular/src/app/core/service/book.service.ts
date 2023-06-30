import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AddBookDTO, Book, BookDetails, UpdateBookDTO } from '../model/book.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class BookService {
  // private baseUrl = "https://library-hub.crabdance.com/api/";
  private baseUrl = "http://localhost:8080/api/";

  constructor(private httpClient: HttpClient) { }

  getBooks(): Observable<Book[]> {
    return this.httpClient.get(this.baseUrl + "books") as Observable<Book[]>;
  }

  getPageBooks(pageNo: Number, pageSize: Number): Observable<Book[]> {
    return this.httpClient.get(this.baseUrl + "books?pageNo=" + pageNo.toString() + "&pageSize=" + pageSize.toString()) as Observable<Book[]>;
  }

  getBook(id: string): Observable<BookDetails> {
    return this.httpClient.get(this.baseUrl + "books/" + id) as Observable<BookDetails>;
  }

  getBookWithMinPrice(price: string, pageNo: Number, pageSize: Number): Observable<Book[]> {
    return this.httpClient.get(this.baseUrl + "books?minPrice=" + price + "&pageNo=" + pageNo.toString() + "&pageSize=" + pageSize.toString()) as Observable<Book[]>;
  }

  countBooks(): Observable<Number> {
    return this.httpClient.get(this.baseUrl + "books/count") as Observable<Number>;
  }

  countBooksWithMinPrice(price: string): Observable<Number> {
    return this.httpClient.get(this.baseUrl + "books/countByPrice?minPrice=" + price) as Observable<Number>;
  }

  addBook(book: AddBookDTO, libraryID: string): Observable<Book> {
    return this.httpClient.post(this.baseUrl + "libraries/" + libraryID + "/books", book) as Observable<Book>;
  }

  updateBook(id: string, book: UpdateBookDTO): Observable<Book> {
    return this.httpClient.put(this.baseUrl + "books/" + id, book) as Observable<Book>;
  }

  deleteBook(id: string): Observable<Object> {
    return this.httpClient.delete(this.baseUrl + "books/" + id) as Observable<Object>;
  }
}
