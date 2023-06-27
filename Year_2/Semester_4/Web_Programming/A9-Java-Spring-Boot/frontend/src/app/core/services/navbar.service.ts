import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class NavbarService {
  private loginSubject = new Subject();
  private logoutSubject = new Subject();

  constructor() { }

  public login() {
    this.loginSubject.next("");
  }

  public logout() {
    this.logoutSubject.next("");
  }

  public getLoginObservable() {
    return this.loginSubject.asObservable();
  }

  public getLogoutObservable() {
    return this.logoutSubject.asObservable();
  }
}
