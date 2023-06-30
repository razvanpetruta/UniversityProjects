import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { UserInfo, UserProfile } from 'src/app/core/model/user.model';
import { StorageService } from 'src/app/core/service/_services/storage.service';
import { UserService } from 'src/app/core/service/_services/user.service';

@Component({
  selector: 'app-update-user-profile',
  templateUrl: './update-user-profile.component.html',
  styleUrls: ['./update-user-profile.component.css']
})
export class UpdateUserProfileComponent implements OnInit {
  isLoggedIn = false;
  username?: string;
  userProfile?: {
    id: number;
    bio: string;
    location: string;
    birthDate: string;
    gender: string;
    maritalStatus: string;
  };

  constructor(
    private storageService: StorageService, 
    private userService: UserService, 
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private toastrService: ToastrService) {}

  ngOnInit(): void {
    this.isLoggedIn = this.storageService.isLoggedIn();

    if (this.isLoggedIn) {
      const user = this.storageService.getUser();
      this.username = user.username;
    } else {
      this.toastrService.error("Login required", "", { progressBar: true });
      this.router.navigateByUrl("/login");
    }

    this.activatedRoute.params.subscribe(params => {
      if (this.username !== params["username"]) {
        this.toastrService.error("You are not allowed to update this profile", "", { progressBar: true });
        this.router.navigateByUrl("/");
      }

      this.userService.getUserProfile(this.username!).subscribe({
        next: (result: UserProfile) => {
          this.userProfile = result.userProfile;
        },
        error: (error) => {
          console.log(error);
        },
        complete: () => {
        }
      });
    });
  }

  onSubmit(form: any): void {
    if (this.username && this.userProfile && this.userProfile.bio && this.userProfile.birthDate && this.userProfile.gender 
      && this.userProfile.id && this.userProfile.location && this.userProfile.maritalStatus) {
      const userInfo: UserInfo = {
        id: this.userProfile.id,
        bio: this.userProfile.bio,
        birthDate: this.userProfile.birthDate,
        gender: this.userProfile.gender,
        maritalStatus: this.userProfile.maritalStatus,
        location: this.userProfile.location
      }
      
      this.userService.updateUserProfile(this.username, userInfo).subscribe({
        next: (userInfo: UserInfo) => {
          this.router.navigateByUrl("/users-active/" + this.username);
        },
        error: (error) => {
          console.log(error.error);
        },
        complete: () => {
        }
      });
    }
  }
}
