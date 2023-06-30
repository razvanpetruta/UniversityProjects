import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AddUpdateLibraryDTO, Library, LibraryAll, LibraryCount, LibraryDetails } from '../model/library.model';

@Injectable({
  providedIn: 'root'
})
export class LibraryService {
  // private baseUrl = "https://library-hub.crabdance.com/api/";
  private baseUrl = "http://localhost:8080/api/";

  constructor(private httpClient: HttpClient) { }

  getLibraries(): Observable<LibraryAll[]> {
    return this.httpClient.get(this.baseUrl + "libraries") as Observable<LibraryAll[]>;
  }

  getPageLibraries(pageNo: Number, pageSize: Number): Observable<LibraryAll[]> {
    return this.httpClient.get(this.baseUrl + "libraries?pageNo=" + pageNo.toString() + "&pageSize=" + pageSize.toString()) as Observable<LibraryAll[]>;
  }

  getLibrary(id: string): Observable<LibraryDetails> {
    return this.httpClient.get(this.baseUrl + "libraries/" + id) as Observable<LibraryDetails>;
  }

  getBooksStatistics(): Observable<LibraryCount[]> {
    return this.httpClient.get(this.baseUrl + "libraries/books-statistic") as Observable<LibraryCount[]>;
  }

  getReadersStatistics(): Observable<LibraryCount[]> {
    return this.httpClient.get(this.baseUrl + "libraries/readers-statistic") as Observable<LibraryCount[]>;
  }

  countLibraries(): Observable<Number> {
    return this.httpClient.get(this.baseUrl + "libraries/count") as Observable<Number>;
  }

  getLibrariesByName(name: string): Observable<Library[]> {
    return this.httpClient.get(this.baseUrl + "libraries-search?name=" + name) as Observable<Library[]>;
  }

  addLibrary(library: AddUpdateLibraryDTO): Observable<Library> {
    return this.httpClient.post(this.baseUrl + "libraries", library) as Observable<Library>;
  }

  updateLibrary(id: string, library: AddUpdateLibraryDTO): Observable<Library> {
    return this.httpClient.put(this.baseUrl + "libraries/" + id, library) as Observable<Library>;
  }

  deleteLibrary(id: string): Observable<Object> {
    return this.httpClient.delete(this.baseUrl + "libraries/" + id) as Observable<Object>;
  }
}
