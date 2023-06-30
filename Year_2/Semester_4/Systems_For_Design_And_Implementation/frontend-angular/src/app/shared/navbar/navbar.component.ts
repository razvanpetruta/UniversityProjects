import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/core/service/_services/auth.service';
import { StorageService } from 'src/app/core/service/_services/storage.service';
import { UserService } from 'src/app/core/service/_services/user.service';
import { NavbarService } from 'src/app/core/service/navbar.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {
  roles: string[] = [];
  isLoggedIn = false;
  username?: string;
  pageSize?: number;

  constructor(
    private storageService: StorageService, 
    private authService: AuthService,
    private navbarService: NavbarService,
    private userService: UserService,
    private router: Router) { }

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

    this.userService.getElementsPerPage().subscribe({
      next: (response) => {
        this.pageSize = response;
      }
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

  isUser(): boolean {
    return this.roles.includes("ROLE_USER");
  }

  isModerator(): boolean {
    return this.roles.includes("ROLE_MODERATOR");
  }

  isAdmin(): boolean {
    return this.roles.includes("ROLE_ADMIN");
  }

  seeAllLibraries(): void {
    this.userService.getElementsPerPage().subscribe({
      next: (response) => {
        this.pageSize = response;
      },
      complete: () => {
        this.router.navigate(['/libraries'], { queryParams: { pageNo: 0, pageSize: this.pageSize } });
      }
    });
  }

  seeAllBooks(): void {
    this.userService.getElementsPerPage().subscribe({
      next: (response) => {
        this.pageSize = response;
      },
      complete: () => {
        this.router.navigate(['/books'], { queryParams: { pageNo: 0, pageSize: this.pageSize } });
      }
    });
  }

  seeAllReaders(): void {
    this.userService.getElementsPerPage().subscribe({
      next: (response) => {
        this.pageSize = response;
      },
      complete: () => {
        this.router.navigate(['/readers'], { queryParams: { pageNo: 0, pageSize: this.pageSize } });
      }
    });
  }
}
