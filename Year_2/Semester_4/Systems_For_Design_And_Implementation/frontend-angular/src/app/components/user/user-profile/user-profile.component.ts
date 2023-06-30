import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UserProfile } from 'src/app/core/model/user.model';
import { StorageService } from 'src/app/core/service/_services/storage.service';
import { UserService } from 'src/app/core/service/_services/user.service';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css'],
})
export class UserProfileComponent implements OnInit {
  isLoggedIn = false;
  id?: number;
  username?: string;
  roles?: [
    {
      name: string;
      id: number;
    }
  ];
  userProfile?: {
    id: number;
    bio: string;
    location: string;
    birthDate: string;
    gender: string;
    maritalStatus: string;
  };
  totalLibraries?: number;
  totalBooks?: number;
  totalReaders?: number;

  showLoader: boolean = true;

  constructor(private storageService: StorageService, private userService: UserService, private activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.showLoader = true;

    this.isLoggedIn = this.storageService.isLoggedIn();

    if (this.isLoggedIn) {
      const user = this.storageService.getUser();
      this.username = user.username;
    }

    this.activatedRoute.params.subscribe(params => {
      this.username = params['username'];
      this.userService.getUserProfile(this.username!).subscribe({
        next: (result: UserProfile) => {
          this.id = result.id;
          this.roles = result.roles;
          this.userProfile = result.userProfile;
          this.totalLibraries = result.totalLibraries;
          this.totalBooks = result.totalBooks;
          this.totalReaders = result.totalReaders;
        },
        error: (error) => {
          this.showLoader = false;
        },
        complete: () => {
          this.showLoader = false;
        }
      });
    });
  }

  isUser(): boolean {
    if (!this.roles) {
      return false;
    }

    let found = false;

    this.roles.forEach((role) => {
      if (role.name === "ROLE_USER") {
        found = true;
      }
    });

    return found;
  }

  isModerator(): boolean {
    if (!this.roles) {
      return false;
    }

    let found = false;

    this.roles.forEach((role) => {
      if (role.name === "ROLE_MODERATOR") {
        found = true;
      }
    });

    return found;
  }

  isAdmin(): boolean {
    if (!this.roles) {
      return false;
    }

    let found = false;

    this.roles.forEach((role) => {
      if (role.name === "ROLE_ADMIN") {
        found = true;
      }
    });

    return found;
  }
}
