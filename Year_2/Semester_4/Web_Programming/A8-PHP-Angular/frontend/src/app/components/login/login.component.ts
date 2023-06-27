import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { NavbarService } from 'src/app/core/service/navbar.service';
import { StorageService } from 'src/app/core/service/storage.service';

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

  constructor(private http: HttpClient, private storageService: StorageService, private navbarService: NavbarService, private router: Router) {}

  login(username: string, password: string) {
    const url = 'http://localhost/angularLab8/php/login.php';
    const formData = new FormData();
    formData.append('user', username);
    formData.append('password', password);
    return this.http.post(url, formData);
  }

  onSubmit() {
    if (!this.username || !this.password) {
      return;
    }

    const username = this.username;
    const password = this.password;

    this.login(username, password).subscribe(response => {
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
