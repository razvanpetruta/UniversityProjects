import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';

const API = '/api/'; 

@Injectable({
  providedIn: 'root',
})
export class GeneralService {
  constructor(private http: HttpClient, private router: Router) {}

  getAllCities(): Observable<any> {
    return this.http.get(API + 'cities');
  }

  getNeighbours(id: string): Observable<any> {
    return this.http.get(API + 'neighbours/' + id);
  }

  getCityById(id: string): Observable<any> {
    return this.http.get(API + 'cities/' + id);
  }
}