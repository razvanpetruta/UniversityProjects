import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from 'src/app/core/service/_services/auth.service';
import { StorageService } from 'src/app/core/service/_services/storage.service';

@Component({
  selector: 'app-register-confirmation',
  templateUrl: './register-confirmation.component.html',
  styleUrls: ['./register-confirmation.component.css']
})
export class RegisterConfirmationComponent implements OnInit {
  roles: string[] = [];
  isLoggedIn = false;
  username?: string;
  jwtToken?: string;
  seconds: number = 10 * 60;
  minutes: number = 10;

  constructor(
    private storageService: StorageService, 
    private router: Router, 
    private activatedRoute: ActivatedRoute,
    private authService: AuthService,
    private toastrService: ToastrService) {}

  ngOnInit(): void {
    this.isLoggedIn = this.storageService.isLoggedIn();

    if (this.isLoggedIn) {
      this.router.navigateByUrl("/");
    }

    this.activatedRoute.params.subscribe(params => {
      this.jwtToken = params['code'];
    });

    const countdown = setInterval(() => {
      if (this.seconds === 0) {
        clearInterval(countdown);
        this.toastrService.error("Registration confirmation expired", "", { progressBar: true });
        this.router.navigateByUrl("/");
      } else {
        this.seconds--;
        this.minutes = Math.floor(this.seconds / 60);
      }
    }, 1000);
  }

  confirmActivation(): void {
    this.authService.confirmRegister(this.jwtToken!).subscribe({
      next: (result: string) => {
        this.toastrService.success("Registration complete", "", { progressBar: true });
        this.router.navigateByUrl("/login");
      },
      error: (error) => {
        console.log(error);
      },
      complete: () => {
      }
    });
  }
}
