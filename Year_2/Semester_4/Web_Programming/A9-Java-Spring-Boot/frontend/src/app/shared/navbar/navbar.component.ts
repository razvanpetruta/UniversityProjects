import { Component } from '@angular/core';
import { AuthService } from 'src/app/core/services/auth.service';
import { NavbarService } from 'src/app/core/services/navbar.service';
import { StorageService } from 'src/app/core/services/storage.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {
  roles?: string[];
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
      this.roles = user.roles;

      this.username = user.username;
    } else {
      this.username = undefined;
    }
  }

  logout(): void {
    const user = this.storageService.getUser();
    this.authService.logout().subscribe({
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
}
