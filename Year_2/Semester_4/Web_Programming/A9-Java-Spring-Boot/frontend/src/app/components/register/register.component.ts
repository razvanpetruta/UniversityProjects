import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from 'src/app/core/services/auth.service';
import { StorageService } from 'src/app/core/services/storage.service';

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

  constructor(private authService: AuthService, private storageService: StorageService, private router: Router, private toastrService: ToastrService) { }

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
        console.log(data);
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
        this.router.navigateByUrl("/login");
        this.toastrService.success("Successfully registered", "", { progressBar: true });
      }
    });
  }
}
