import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Subject, debounceTime } from 'rxjs';
import { Library } from 'src/app/core/model/library.model';
import { Membership, ReaderDetails } from 'src/app/core/model/reader.model';
import { StorageService } from 'src/app/core/service/_services/storage.service';
import { LibraryService } from 'src/app/core/service/library.service';
import { ReaderService } from 'src/app/core/service/reader.service';

@Component({
  selector: 'app-reader-new-membership',
  templateUrl: './reader-new-membership.component.html',
  styleUrls: ['./reader-new-membership.component.css']
})
export class ReaderNewMembershipComponent {
  showSearchLoader: boolean = false;
  showLoader: boolean = false;

  selectedOption?: string;
  selectedLibrary?: Library;
  searchTerm = new Subject<string>();
  options?: Library[];
  reader?: ReaderDetails;

  roles: string[] = [];
  isLoggedIn = false;
  username?: string;

  constructor(
    private readerService: ReaderService, 
    private libraryService: LibraryService, 
    private router: Router, 
    private activatedRoute: ActivatedRoute,
    private toastrService: ToastrService,
    private storageService: StorageService) { }

  ngOnInit() {
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
      this.readerService.getReader(params["id"]).subscribe({
        next: (reader: ReaderDetails) => {
          this.reader = reader;
          if (this.username !== reader.username && !this.isModerator() && !this.isAdmin()) {
            this.toastrService.error("You are not allowed to create a membership for this user", "", { progressBar: true });
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
      });
    });

    this.searchTerm.pipe(
      debounceTime(1000) // debounce by 1 second
    ).subscribe(term => {
      this.showSearchLoader = true;

      if (term.trim()) {
        this.libraryService.getLibrariesByName(term).subscribe({
          next: (libraries: Library[]) => {
            this.options = libraries;
          },
          error: (error) => {
            this.showLoader = false;
            this.router.navigateByUrl("/");
            this.toastrService.error(error.error, "", { progressBar: true });
          },
          complete: () => {
            this.showSearchLoader = false;
          }
        });
      } else {
        this.options = undefined;
        this.showSearchLoader = false;
      }
    });
  }

  onSubmit(form: any): void {
    this.showLoader = true;

    if (this.selectedLibrary && this.reader) {
      this.readerService.createMembership(this.selectedLibrary.id.toString(), this.reader.id.toString()).subscribe({
        next: (membership: Membership) => {
          this.router.navigateByUrl("/readers/" + membership.id.readerID);
        },
        error: (error) => {
          this.showLoader = false;
          this.router.navigateByUrl("/");
          this.toastrService.error(error.error, "", { progressBar: true });
        },
        complete: () => {
          this.showLoader = false;
          this.toastrService.success("Membership created successfully", "", { progressBar: true });
        }
      });
    }
  }

  onSelection(event: any): void {
    this.selectedOption = event.option.value.name;
    this.selectedLibrary = event.option.value;
  }

  search(term: string): void {
    this.searchTerm.next(term);
  }

  isModerator(): boolean {
    return this.roles.includes("ROLE_MODERATOR");
  }

  isAdmin(): boolean {
    return this.roles.includes("ROLE_ADMIN");
  }
}
