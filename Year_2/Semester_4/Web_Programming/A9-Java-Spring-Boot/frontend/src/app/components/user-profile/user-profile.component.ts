import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { GeneralService } from 'src/app/core/services/general.service';
import { StorageService } from 'src/app/core/services/storage.service';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent {
  isLoggedIn = false;
  id?: number;
  username?: string;
  roles?: string[];
  userProfile?: {
    id: number;
    name: string;
    email: string;
    imageURL: string;
    age: number;
    homeTown: string;
  };

  constructor(
    private storageService: StorageService, 
    private generalService: GeneralService, 
    private activatedRoute: ActivatedRoute,
    private router: Router) {}

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
}
