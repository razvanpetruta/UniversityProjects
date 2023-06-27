import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/core/service/auth.service';
import { NavbarService } from 'src/app/core/service/navbar.service';
import { StorageService } from 'src/app/core/service/storage.service';

@Component({
  selector: 'app-main-page',
  templateUrl: './main-page.component.html',
  styleUrls: ['./main-page.component.css']
})
export class MainPageComponent implements OnInit {
  role?: string;
  isLoggedIn = false;
  username?: string;

  constructor(
    private storageService: StorageService,
    private authService: AuthService,
    private navbarService: NavbarService) { }

    ngOnInit(): void {
      this.initialiseNavbar();
  
      this.navbarService.getLoginObservable().subscribe(() => {
        this.isLoggedIn = true;
        this.initialiseNavbar();
      });
  
      this.navbarService.getLogoutObservable().subscribe(() => {
        this.isLoggedIn = false;
        this.initialiseNavbar();
      });
    }
  
    initialiseNavbar(): void {
      this.isLoggedIn = this.storageService.isLoggedIn();
  
      if (this.isLoggedIn) {
        const user = this.storageService.getUser();
        this.role = user.role;
  
        this.username = user.username;
      } else {
        this.username = undefined;
      }
    }
  
  isUser(): boolean {
    return this.role === "USER";
  }

  isAdmin(): boolean {
    return this.role === "ADMIN";
  }
}
