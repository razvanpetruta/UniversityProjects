import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';

const AUTH_API = '/api/';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor(private http: HttpClient, private router: Router) {}

  logout(): Observable<any> {
    this.router.navigateByUrl("/");
    return this.http.post(AUTH_API + 'users/logout', { });
  }
}