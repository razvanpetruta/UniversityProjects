import { Component } from '@angular/core';
import { Subject } from 'rxjs';
import { AuthService } from 'src/app/core/service/auth.service';
import { NavbarService } from 'src/app/core/service/navbar.service';
import { StorageService } from 'src/app/core/service/storage.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {
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

      this.username = user.user;
    } else {
      this.username = undefined;
    }
  }

  logout(): void {
    const user = this.storageService.getUser();
    this.authService.logout(user.session_id).subscribe({
      next: res => {
        this.storageService.clean();
      },
      error: err => {
        console.log(err);
      },
      complete: () => {
        this.navbarService.logout();
      }
    });
  }

  isUser(): boolean {
    return this.role === "USER";
  }

  isAdmin(): boolean {
    return this.role === "ADMIN";
  }
}
