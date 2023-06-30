import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { AddUpdateLibraryDTO, Library, LibraryDetails } from 'src/app/core/model/library.model';
import { StorageService } from 'src/app/core/service/_services/storage.service';
import { LibraryService } from 'src/app/core/service/library.service';

@Component({
  selector: 'app-library-update',
  templateUrl: './library-update.component.html',
  styleUrls: ['./library-update.component.css']
})
export class LibraryUpdateComponent implements OnInit {
  libraryID?: string;
  name?: string;
  address?: string;
  email?: string;
  website?: string;
  yearOfConstruction?: number;
  showLoader: boolean = false;
  errorMessages = {
    'name': null,
    'address': null,
    'email': null,
    'website': null,
    'yearOfConstruction': null
  };

  roles: string[] = [];
  isLoggedIn = false;
  username?: string;

  constructor(
    private libraryService: LibraryService, 
    private activatedRoute: ActivatedRoute, 
    private router: Router,
    private storageService: StorageService,
    private toastrService: ToastrService) {}

  ngOnInit(): void {
    this.isLoggedIn = this.storageService.isLoggedIn();

    if (this.isLoggedIn) {
      const user = this.storageService.getUser();
      this.roles = user.roles;

      this.username = user.username;
    } else {
      this.toastrService.error("Log in required", "", { progressBar: true });
      this.router.navigateByUrl("/login");
    }

    this.showLoader = true;

    this.activatedRoute.params.subscribe(params => {
      this.libraryID = params['id'];
      this.libraryService.getLibrary(this.libraryID!).subscribe({
        next: (library: LibraryDetails) => {
          this.name = library.name;
          this.address = library.address;
          this.email = library.email;
          this.website = library.website;
          this.yearOfConstruction = library.yearOfConstruction;
          if (this.username !== library.username && !this.isModerator() && !this.isAdmin()) {
            this.toastrService.error("You are not allowed to update this library", "", { progressBar: true });
            this.router.navigateByUrl("/");
          }
        },
        error: (error) => {
          this.showLoader = false;
          this.router.navigateByUrl("/");
          this.toastrService.error(error.error, "", { progressBar: true });
        },
        complete: () => {
          this.showLoader = false;
        }
      }
    );
    });
  }

  onSubmit(form: any): void {
    this.showLoader = true;

    if (this.name && this.address && this.email && this.website && this.yearOfConstruction) {
      const library: AddUpdateLibraryDTO = {
        name: this.name,
        address: this.address,
        email: this.email,
        website: this.website,
        yearOfConstruction: this.yearOfConstruction
      }
      
      this.libraryService.updateLibrary(this.libraryID!, library).subscribe({
        next: (library: Library) => {
          this.router.navigateByUrl("/libraries/" + library.id);
        },
        error: (error) => {
          this.showLoader = false;

          if (error.status === 400) {
            const errors = error.error.errors;

            if ('name' in errors) {
              this.errorMessages.name = errors.name;
            } else {
              this.errorMessages.name = null;
            }

            if ('email' in errors) {
              this.errorMessages.email = errors.email;
            } else {
              this.errorMessages.email = null;
            }

            if ('address' in errors) {
              this.errorMessages.address = errors.address;
            } else {
              this.errorMessages.address = null;
            }

            if ('website' in errors) {
              this.errorMessages.website = errors.website;
            } else {
              this.errorMessages.website = null;
            }

            if ('yearOfConstruction' in errors) {
              this.errorMessages.yearOfConstruction = errors.yearOfConstruction;
            } else {
              this.errorMessages.yearOfConstruction = null;
            }
          }
        },
        complete: () => {
          this.showLoader = false;
          this.toastrService.success("Library updated succesfully", "", { progressBar: true });
        }
      });
    }
  }

  isUser(): boolean {
    return this.roles.includes("ROLE_USER");
  }

  isModerator(): boolean {
    return this.roles.includes("ROLE_MODERATOR");
  }

  isAdmin(): boolean {
    return this.roles.includes("ROLE_ADMIN");
  }
}
