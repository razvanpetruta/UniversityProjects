import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';

const AUTH_API = 'http://localhost/angularLab8/php/';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor(private http: HttpClient, private router: Router) {}

  login(username: string, password: string) {
    const url = AUTH_API + 'login.php';
    const formData = new FormData();
    formData.append('user', username);
    formData.append('password', password);
    return this.http.post(url, formData);
  }

  logout(session_id: string): Observable<any> {
    this.router.navigateByUrl("/");
    return this.http.post(AUTH_API + 'logout.php', { 'session_id': session_id });
  }
}