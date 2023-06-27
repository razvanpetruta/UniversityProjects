import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from 'src/app/core/service/auth.service';
import { NavbarService } from 'src/app/core/service/navbar.service';
import { StorageService } from 'src/app/core/service/storage.service';

@Component({
  selector: 'app-authenticate',
  templateUrl: './authenticate.component.html',
  styleUrls: ['./authenticate.component.css']
})
export class AuthenticateComponent {
  name?: string;
  errorMessage?: string;
  isLoginFailed?: boolean;

  constructor(
    private storageService: StorageService,
    private navbarService: NavbarService,
    private router: Router,
    private authService: AuthService,
    private toastrService: ToastrService
  ) {}

  onSubmit() {
    if (!this.name) {
      return;
    }

    const name = this.name;

    this.authService.login(name).subscribe({
      next: (response: any) => {
        if (response.statusCode !== 404) {
          this.storageService.saveUser(response.value);
          this.navbarService.login();
          this.isLoginFailed = false;
          this.router.navigateByUrl('/');
        } else {
          this.toastrService.error(response.value, "", { progressBar: true });
        }
      },
      error: (error) => {
        this.errorMessage = error.error.error;
        this.isLoginFailed = true;
      },
    });
  }
}
