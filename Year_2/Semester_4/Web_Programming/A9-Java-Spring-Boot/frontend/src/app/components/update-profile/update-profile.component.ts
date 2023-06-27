import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { GeneralService } from 'src/app/core/services/general.service';
import { StorageService } from 'src/app/core/services/storage.service';

@Component({
  selector: 'app-update-profile',
  templateUrl: './update-profile.component.html',
  styleUrls: ['./update-profile.component.css']
})
export class UpdateProfileComponent {
  isLoggedIn = false;
  username?: string;
  userProfile?: {
    id: number;
    name: string;
    email: string;
    age: number;
    homeTown: string;
    imageURL: string;
  };

  constructor(
    private storageService: StorageService, 
    private generalService: GeneralService, 
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private toastrService: ToastrService) {}

  ngOnInit(): void {
    this.isLoggedIn = this.storageService.isLoggedIn();

    if (this.isLoggedIn) {
      const user = this.storageService.getUser();
      this.username = user.username;
    } else {
      this.router.navigateByUrl("/login");
    }

    this.activatedRoute.params.subscribe(params => {
      if (this.username !== params["username"]) {
        this.router.navigateByUrl("/");
      }
    });
      
    this.generalService.getUserProfile(this.username!).subscribe({
      next: (response) => {
        this.userProfile = response;
      }
    });
  }

  onSubmit(form: any): void {
    if (this.username && this.userProfile) {
      if (this.userProfile.name.length < 3) {
        this.toastrService.error("Name must be at least 3 characters", "", { progressBar: true });
        return;
      }

      if (!this.userProfile.email.includes("@")) {
        this.toastrService.error("Make sure the email is in correct format", "", { progressBar: true });
        return;
      }

      if (this.userProfile.age > 100 || this.userProfile.age < 10) {
        this.toastrService.error("The age must be between 10 and 100", "", { progressBar: true });
        return;
      }

      this.generalService.updateUserProfile(this.username, this.userProfile.name, this.userProfile.email, this.userProfile.age, this.userProfile.homeTown, this.userProfile.imageURL).subscribe({
        next: (res) => {
        },
        complete: () => {
          this.router.navigateByUrl("/users/" + this.username);
          this.toastrService.success("Successfully updated the profile", "", { progressBar: true });
        }
      });
    }
  }
}
