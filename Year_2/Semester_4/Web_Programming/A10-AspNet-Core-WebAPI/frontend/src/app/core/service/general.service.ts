import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';

const AUTH_API = '/api/';

@Injectable({
  providedIn: 'root',
})
export class GeneralService {
  constructor(private http: HttpClient, private router: Router) {}

  getGenres(): Observable<any> {
    return this.http.get(AUTH_API + "books/genres");
  }

  getFilteredBooks(genre: string): Observable<any> {
    return this.http.get(AUTH_API + "books/genre/" + genre);
  }

  getBookByID(id: number): Observable<any> {
    return this.http.get(AUTH_API + "books/" + id);
  }

  getRentals(username: string): Observable<any> {
    return this.http.get(AUTH_API + "rentals/approved/" + username);
  }

  getUnapprovedRentals(): Observable<any> {
    return this.http.get(AUTH_API + "rentals/unapproved");
  }

  requestBook(bookID: number): Observable<any> {
    return this.http.post(AUTH_API + "rentals/request?bookID=" + bookID, { 'id': bookID });
  }

  approveRental(rentalID: number): Observable<any> {
    return this.http.put(AUTH_API + "rentals/" + rentalID + "/approve", { });
  }

  rejectRental(rentalID: number): Observable<any> {
    return this.http.delete(AUTH_API + "rentals/" + rentalID, { });
  }

  addBook(title: string, author: string, noPages: number, price: number, genre: string): Observable<any> {
    return this.http.post(AUTH_API + "books", { 'title': title, 'author': author, 'noPages': noPages, 'price': price, 'genre': genre });
  }

  updateBook(bookID: number, title: string, author: string, noPages: number, price: number, genre: string): Observable<any> {
    return this.http.put(AUTH_API + "books/" + bookID.toString(), { 'id': bookID, 'title': title, 'author': author, 'noPages': noPages, 'price': price, 'genre': genre });
  }

  deleteBook(bookID: number): Observable<any> {
    return this.http.delete(AUTH_API + "books/" + bookID, { });
  }
}