import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Library, LibraryDetails } from 'src/app/core/model/library.model';
import { StorageService } from 'src/app/core/service/_services/storage.service';
import { LibraryService } from 'src/app/core/service/library.service';

@Component({
  selector: 'app-library-delete',
  templateUrl: './library-delete.component.html',
  styleUrls: ['./library-delete.component.css']
})
export class LibraryDeleteComponent implements OnInit {
  library?: LibraryDetails;
  libraryID?: string;
  consent: boolean = false;
  showLoader: boolean = true;

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

      if (!this.isAdmin()) {
        this.toastrService.error("You are not an admin", "", { progressBar: true });
        this.router.navigateByUrl("/login");  
      }
    } else {
      this.toastrService.error("Log in required", "", { progressBar: true });
      this.router.navigateByUrl("/login");
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

  onDelete(): void {
    this.showLoader = true;

    this.libraryService.deleteLibrary(this.libraryID!).subscribe({
      next: (library: Object) => {
        this.router.navigate(['/libraries'], { queryParams: { pageNo: 0, pageSize: 25 } });
      },
      error: (error) => {
        this.showLoader = false;
        this.router.navigateByUrl("/");
        this.toastrService.error(error.error, "", { progressBar: true });
      },
      complete: () => {
        this.showLoader = false;
        this.toastrService.success("Library successfully deleted", "", { progressBar: true });
      }
    });
  }

  onCancel(): void {
    this.router.navigate(['/libraries'], { queryParams: { pageNo: 0, pageSize: 25 } });
  }

  isAdmin(): boolean {
    return this.roles.includes("ROLE_ADMIN");
  }
}
