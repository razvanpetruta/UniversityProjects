import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SQLResponse, UserInfo, UserProfile, UserRolesUpdate, UserWithRolesDTO } from '../../model/user.model';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private baseUrl = "http://localhost:8080/api/";
  // private baseUrl = "https://library-hub.crabdance.com/api/";

  constructor(private httpClient: HttpClient) {}

  getElementsPerPage(): Observable<any> {
    return this.httpClient.get(this.baseUrl + "user-settings");
  }

  updateElementsPerPage(elementsPerPage: number): Observable<any> {
    return this.httpClient.post(this.baseUrl + "user-settings", { 'elementsPerPage': elementsPerPage });
  }

  getUserProfile(username: string): Observable<UserProfile> {
    return this.httpClient.get(this.baseUrl + "user-profile/" + username) as Observable<UserProfile>;
  }

  getUsersByUsername(username: string): Observable<UserWithRolesDTO[]> {
    return this.httpClient.get(this.baseUrl + "users-search?username=" + username) as Observable<UserWithRolesDTO[]>;
  }

  updateUserProfile(username: string, userProfile: UserInfo): Observable<UserInfo> {
    return this.httpClient.put(this.baseUrl + "user-profile/" + username, userProfile) as Observable<UserInfo>;
  }

  updateUserRoles(username: string, roles: UserRolesUpdate): Observable<UserWithRolesDTO> {
    return this.httpClient.put(this.baseUrl + "user-update-roles/" + username, roles) as Observable<UserWithRolesDTO>;
  }

  deleteAllLibraries(): Observable<SQLResponse> {
    return this.httpClient.post(this.baseUrl + "run-delete-libraries-script", {}) as Observable<SQLResponse>;
  }

  deleteAllBooks(): Observable<SQLResponse> {
    return this.httpClient.post(this.baseUrl + "run-delete-books-script", {}) as Observable<SQLResponse>;
  }

  deleteAllReaders(): Observable<SQLResponse> {
    return this.httpClient.post(this.baseUrl + "run-delete-readers-script", {}) as Observable<SQLResponse>;
  }

  deleteAllMemberships(): Observable<SQLResponse> {
    return this.httpClient.post(this.baseUrl + "run-delete-memberships-script", {}) as Observable<SQLResponse>;
  }

  insertAllBooks(): Observable<SQLResponse> {
    return this.httpClient.post(this.baseUrl + "run-insert-books-script", {}) as Observable<SQLResponse>;
  }

  insertAllLibraries(): Observable<SQLResponse> {
    return this.httpClient.post(this.baseUrl + "run-insert-libraries-script", {}) as Observable<SQLResponse>;
  }

  insertAllReaders(): Observable<SQLResponse> {
    return this.httpClient.post(this.baseUrl + "run-insert-readers-script", {}) as Observable<SQLResponse>;
  }

  insertAllMemberships(): Observable<SQLResponse> {
    return this.httpClient.post(this.baseUrl + "run-insert-memberships-script", {}) as Observable<SQLResponse>;
  }
}