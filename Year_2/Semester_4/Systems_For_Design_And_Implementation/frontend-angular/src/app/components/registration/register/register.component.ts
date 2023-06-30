import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { RegisterResponseDTO } from 'src/app/core/model/user.model';
import { AuthService } from 'src/app/core/service/_services/auth.service';
import { StorageService } from 'src/app/core/service/_services/storage.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  form: any = {
    username: null,
    password: null,
    confirmPassword: null
  };
  isLoggedIn = false;
  registeredFailed = false;
  errorMessage = '';
  usernameErrorMessage = undefined;
  passwordErrorMessage = undefined;
  registerInfo?: RegisterResponseDTO;

  constructor(private authService: AuthService, private storageService: StorageService, private router: Router) { }

  ngOnInit(): void {
    if (this.storageService.isLoggedIn()) {
      this.isLoggedIn = true;
      this.router.navigateByUrl("/");
    }
  }

  onSubmit(): void {
    const { username, password } = this.form;

    this.authService.register(username, password).subscribe({
      next: data => {
        this.registeredFailed = false;
        this.registerInfo = data;
        this.router.navigate(["/register-confirmation", data.jwtToken]);
      },
      error: err => {
        this.registeredFailed = true;
        this.errorMessage = err.error.message ? err.error.message : undefined;
        if (err.error.hasOwnProperty('errors')) {
          this.usernameErrorMessage = err.error.errors.hasOwnProperty('username') ? err.error.errors.username : undefined;
          this.passwordErrorMessage = err.error.errors.hasOwnProperty('password') ? err.error.errors.password : undefined;
        } else {
          this.usernameErrorMessage = undefined;
          this.passwordErrorMessage = undefined;
        }
      },
      complete: () => {
        this.registeredFailed = false;
      }
    });
  }
}
