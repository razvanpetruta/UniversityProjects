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

  getUserInfo(): Observable<any> {
    return this.http.get(AUTH_API + "userinfo");
  }

  getUserProfile(username: string): Observable<any> {
    return this.http.get(AUTH_API + "user-profile/" + username);
  }

  updateUserProfile(username: string, name: string, email: string, age: number, homeTown: string, imageURL: string): Observable<any> {
    return this.http.put(AUTH_API + "user-profile/" + username, { 'name': name, 'email': email, 'age': age, 'homeTown': homeTown, 'imageURL': imageURL });
  }

  searchUserProfiles(name: string, email: string, homeTown: string, maxAge: number): Observable<any> {
    return this.http.get(AUTH_API + "search?name=" + name + "&email=" + email + "&homeTown=" + homeTown + "&maxAge=" + maxAge);
  }
}