import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { LibraryDetails } from 'src/app/core/model/library.model';
import { StorageService } from 'src/app/core/service/_services/storage.service';
import { LibraryService } from 'src/app/core/service/library.service';

@Component({
  selector: 'app-library-details',
  templateUrl: './library-details.component.html',
  styleUrls: ['./library-details.component.css']
})
export class LibraryDetailsComponent implements OnInit {
  library?: LibraryDetails;
  libraryID?: string;
  showLoader: boolean = false;

  roles: string[] = [];
  isLoggedIn = false;
  username?: string;

  constructor(
    private libraryService: LibraryService, 
    private activatedRoute: ActivatedRoute,
    private storageService: StorageService,
    private router: Router,
    private toastrService: ToastrService) {}

  ngOnInit(): void {
    this.isLoggedIn = this.storageService.isLoggedIn();

    if (this.isLoggedIn) {
      const user = this.storageService.getUser();
      this.roles = user.roles;

      this.username = user.username;
    }

    this.showLoader = true;

    this.activatedRoute.params.subscribe(params => {
      this.libraryID = params['id'];
      this.libraryService.getLibrary(this.libraryID!).subscribe({
        next: (result: LibraryDetails) => {
          this.library = result;
        },
        error: (error) => {
          this.showLoader = false;
          this.router.navigateByUrl("/");
          this.toastrService.error(error.error, "", { progressBar: true });
        },
        complete: () => {
          this.showLoader = false;
        }
      });
    });
  }

  isActive(date: Date): boolean {
    return !(new Date(date).getTime() < new Date().getTime());
  }

  isUser(): boolean {
    return this.roles.includes("ROLE_USER");
  }

  isCorrectUser(): boolean {
    if (!this.library) {
      return false;
    }

    return this.username === this.library.username;
  }

  isModerator(): boolean {
    return this.roles.includes("ROLE_MODERATOR");
  }

  isAdmin(): boolean {
    return this.roles.includes("ROLE_ADMIN");
  }
}
