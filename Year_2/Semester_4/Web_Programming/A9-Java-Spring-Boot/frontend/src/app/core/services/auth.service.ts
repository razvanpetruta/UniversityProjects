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

  register(username: string, password: string): Observable<any> {
    return this.http.post(
      AUTH_API + 'register',
      {
        "username": username,
        "password": password,
      });
  }

  login(username: string, password: string) {
    const url = AUTH_API + 'login';
    return this.http.post(url, { 'username': username, 'password': password });
  }

  logout(): Observable<any> {
    this.router.navigateByUrl("/");
    return this.http.post(AUTH_API + 'logout', { });
  }
}