import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { GeneralService } from 'src/app/core/services/general.service';
import { StorageService } from 'src/app/core/services/storage.service';

@Component({
  selector: 'app-search-profiles',
  templateUrl: './search-profiles.component.html',
  styleUrls: ['./search-profiles.component.css'],
})
export class SearchProfilesComponent implements OnInit {
  isLoggedIn = false;
  id?: number;
  username?: string;
  roles?: string[];
  userProfile = {
    name: '',
    email: '',
    age: 100,
    homeTown: '',
  };
  profiles?: [
    {
      id: number;
      name: string;
      email: string;
      homeTown: string;
      imageURL: string;
      age: number;
    }
  ];

  constructor(
    private storageService: StorageService,
    private generalService: GeneralService,
    private activatedRoute: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.isLoggedIn = this.storageService.isLoggedIn();

    if (this.isLoggedIn) {
      const user = this.storageService.getUser();
      this.username = user.username;
    } else {
      this.router.navigateByUrl('/login');
    }
  }

  onSubmit(form: any): void {
    if (this.userProfile) {
      this.generalService
        .searchUserProfiles(
          this.userProfile.name,
          this.userProfile.email,
          this.userProfile.homeTown,
          this.userProfile.age
        )
        .subscribe({
          next: (res) => {
            this.profiles = res;
          },
        });
    }
  }
}
