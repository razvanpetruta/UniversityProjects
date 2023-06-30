import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RegisterResponseDTO } from '../../model/user.model';
import { Router } from '@angular/router';

const AUTH_API = 'http://localhost:8080/api/auth/';
// const AUTH_API = "https://library-hub.crabdance.com/api/auth/";

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
  withCredentials: true
};

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor(private http: HttpClient, private router: Router) {}

  login(username: string, password: string): Observable<any> {
    return this.http.post(
      AUTH_API + 'signin',
      {
        "username": username,
        "password": password,
      },
      httpOptions
    );
  }

  register(username: string, password: string): Observable<RegisterResponseDTO> {
    return this.http.post(
      AUTH_API + 'register',
      {
        "username": username,
        "password": password,
      },
      httpOptions
    ) as Observable<RegisterResponseDTO>;
  }

  confirmRegister(jwtToken: string): Observable<any> {
    return this.http.post(AUTH_API + "register/confirm/" + jwtToken, { }, httpOptions);
  }

  logout(): Observable<any> {
    this.router.navigateByUrl("/");
    return this.http.post(AUTH_API + 'signout', { }, httpOptions);
  }
}