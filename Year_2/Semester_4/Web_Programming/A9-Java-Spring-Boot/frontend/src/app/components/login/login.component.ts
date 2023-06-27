import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/core/services/auth.service';
import { NavbarService } from 'src/app/core/services/navbar.service';
import { StorageService } from 'src/app/core/services/storage.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  username?: string;
  password?: string;
  errorMessage?: string;
  isLoginFailed?: boolean;

  constructor(private http: HttpClient, 
    private storageService: StorageService, 
    private navbarService: NavbarService, 
    private router: Router,
    private authService: AuthService) {}

  onSubmit() {
    if (!this.username || !this.password) {
      return;
    }

    const username = this.username;
    const password = this.password;

    this.authService.login(username, password).subscribe(response => {
      this.storageService.saveUser(response);
      this.navbarService.login();
      this.isLoginFailed = false;
      this.router.navigateByUrl("/");
    }, error => {
      this.errorMessage = error.error.error;
      this.isLoginFailed = true;
    });
  }
}
