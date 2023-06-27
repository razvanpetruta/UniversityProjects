import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';

const AUTH_API = 'http://localhost/angularLab8/php/';

@Injectable({
  providedIn: 'root',
})
export class GeneralService {
  constructor(private http: HttpClient, private router: Router) {}

  getGenres(): Observable<any> {
    return this.http.get(AUTH_API + "getGenres.php");
  }

  getFilteredBooks(genre: string): Observable<any> {
    return this.http.get(AUTH_API + "getFilteredBooks.php?genre=" + genre);
  }

  getBookByID(id: number): Observable<any> {
    return this.http.get(AUTH_API + "getBookByID.php?id=" + id);
  }

  getRentals(session_id: string): Observable<any> {
    return this.http.get(AUTH_API + "getRentedBooks.php?session_id=" + session_id);
  }

  getUnapprovedRentals(): Observable<any> {
    return this.http.get(AUTH_API + "getUnapprovedRentals.php");
  }

  requestBook(bookID: number, session_id: string): Observable<any> {
    return this.http.post(AUTH_API + "requestBook.php", { 'id': bookID, 'session_id': session_id });
  }

  approveRental(rentalID: number, session_id: string): Observable<any> {
    return this.http.post(AUTH_API + "approveRental.php", { 'id': rentalID, 'session_id': session_id });
  }

  rejectRental(rentalID: number, session_id: string): Observable<any> {
    return this.http.post(AUTH_API + "rejectRental.php", { 'id': rentalID, 'session_id': session_id });
  }

  addBook(session_id: string, title: string, author: string, noPages: number, price: number, genre: string): Observable<any> {
    return this.http.post(AUTH_API + "insertBook.php", { 'session_id': session_id, 'title': title, 'author': author, 'noPages': noPages, 'price': price, 'genre': genre });
  }

  updateBook(session_id: string, bookID: number, title: string, author: string, noPages: number, price: number, genre: string): Observable<any> {
    return this.http.post(AUTH_API + "updateBook.php", { 'session_id': session_id, 'id': bookID, 'title': title, 'author': author, 'noPages': noPages, 'price': price, 'genre': genre });
  }

  deleteBook(session_id: string, bookID: number): Observable<any> {
    return this.http.post(AUTH_API + "deleteBook.php", { 'session_id': session_id, 'id': bookID });
  }
}