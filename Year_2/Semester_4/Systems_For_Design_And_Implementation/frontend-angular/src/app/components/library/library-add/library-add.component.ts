import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { AddUpdateLibraryDTO, Library } from 'src/app/core/model/library.model';
import { AuthService } from 'src/app/core/service/_services/auth.service';
import { StorageService } from 'src/app/core/service/_services/storage.service';
import { LibraryService } from 'src/app/core/service/library.service';

@Component({
  selector: 'app-library-add',
  templateUrl: './library-add.component.html',
  styleUrls: ['./library-add.component.css']
})
export class LibraryAddComponent implements OnInit {
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
    private router: Router, 
    private toastrService: ToastrService,
    private storageService: StorageService) {}

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
  }

  onSubmit(form: any) {
    this.showLoader = true;

    if (this.name && this.address && this.email && this.website && this.yearOfConstruction) {
      const library: AddUpdateLibraryDTO = {
        name: this.name,
        address: this.address,
        email: this.email,
        website: this.website,
        yearOfConstruction: this.yearOfConstruction
      }
      
      this.libraryService.addLibrary(library).subscribe({
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

          if (error.status === 401 || error.status === 402 || error.status === 403) {
            this.toastrService.error(error.error.message, "", { progressBar: true });
          }
          
        },
        complete: () => {
          this.showLoader = false;
          this.toastrService.success("Library added successfully", "", { progressBar: true });
        }
      })
    }
  }
}
