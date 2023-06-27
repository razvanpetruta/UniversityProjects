import { Injectable } from '@angular/core';

const USER_KEY = 'auth-user';

@Injectable({
  providedIn: 'root'
})
export class StorageService {
  constructor() {}

  clean(): void {
    window.sessionStorage.clear();
  }

  public saveUser(user: any): void {
    window.sessionStorage.removeItem(USER_KEY);
    window.sessionStorage.setItem(USER_KEY, JSON.stringify(user));
  }

  public getRoute(): any {
    const route = window.sessionStorage.getItem(USER_KEY);
    
    if (route) {
      return JSON.parse(route);
    }

    return {};
  }

  public setRoute(route: any): void {
    window.sessionStorage.clear();
    window.sessionStorage.setItem(USER_KEY, JSON.stringify(route));
  } 

  public getUser(): any {
    const user = window.sessionStorage.getItem(USER_KEY);
    
    if (user) {
      return JSON.parse(user);
    }

    return undefined;
  }

  public isLoggedIn(): boolean {
    const user = window.sessionStorage.getItem(USER_KEY);

    if (user) {
      return true;
    }

    return false;
  }
}