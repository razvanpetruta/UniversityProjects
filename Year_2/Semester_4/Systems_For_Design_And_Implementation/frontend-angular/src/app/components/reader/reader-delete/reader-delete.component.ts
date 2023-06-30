import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { ReaderDetails } from 'src/app/core/model/reader.model';
import { StorageService } from 'src/app/core/service/_services/storage.service';
import { ReaderService } from 'src/app/core/service/reader.service';

@Component({
  selector: 'app-reader-delete',
  templateUrl: './reader-delete.component.html',
  styleUrls: ['./reader-delete.component.css']
})
export class ReaderDeleteComponent implements OnInit {
  reader?: ReaderDetails;
  readerID?: string;
  consent: boolean = false;
  showLoader: boolean = false;

  roles: string[] = [];
  isLoggedIn = false;
  username?: string;

  constructor(
    private readerService: ReaderService, 
    private activatedRoute: ActivatedRoute, 
    private router: Router,
    private toastrService: ToastrService,
    private storageService: StorageService) {}

  ngOnInit(): void {
    this.isLoggedIn = this.storageService.isLoggedIn();

    if (this.isLoggedIn) {
      const user = this.storageService.getUser();
      this.roles = user.roles;

      if (!this.isAdmin()) {
        this.toastrService.error("You are not an admin", "", { progressBar: true });
        this.router.navigateByUrl("/");
      }

      this.username = user.username;
    } else {
      this.toastrService.error("Log in required", "", { progressBar: true });
      this.router.navigateByUrl("/login");
    }

    this.showLoader = true;

    this.activatedRoute.params.subscribe(params => {
      this.readerID = params['id'];
      this.readerService.getReader(this.readerID!).subscribe({
        next: (result: ReaderDetails) => {
          this.reader = result;
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

    this.readerService.deleteReader(this.readerID!).subscribe({
      next: (reader: Object) => {
        this.router.navigate(['/readers'], { queryParams: { pageNo: 0, pageSize: 25 } });
      },
      error: (error) => {
        this.showLoader = false;
        this.router.navigateByUrl("/");
        this.toastrService.error(error.error, "", { progressBar: true });
      },
      complete: () => {
        this.showLoader = false;
        this.toastrService.success("Reader deleted successfully", "", { progressBar: true });
      }
    });
  }

  onCancel(): void {
    this.router.navigate(['/readers'], { queryParams: { pageNo: 0, pageSize: 25 } });
  }

  isAdmin(): boolean {
    return this.roles.includes("ROLE_ADMIN");
  }
}
