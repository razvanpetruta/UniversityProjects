import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { BookDetails } from 'src/app/core/model/book.model';
import { StorageService } from 'src/app/core/service/_services/storage.service';
import { BookService } from 'src/app/core/service/book.service';

@Component({
  selector: 'app-book-delete',
  templateUrl: './book-delete.component.html',
  styleUrls: ['./book-delete.component.css']
})
export class BookDeleteComponent implements OnInit {
  book?: BookDetails;
  bookID?: string;
  consent: boolean = false;
  showLoader: boolean = false;

  roles: string[] = [];
  isLoggedIn = false;
  username?: string;

  constructor(
    private bookService: BookService, 
    private activatedRoute: ActivatedRoute, 
    private router: Router,
    private storageService: StorageService,
    private toastrService: ToastrService) {}

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
      this.bookID = params['id'];
      this.bookService.getBook(this.bookID!).subscribe({
        next: (result: BookDetails) => {
          this.book = result;
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
    this.bookService.deleteBook(this.bookID!).subscribe({
      next: (library: Object) => {
        this.router.navigate(['/books'], { queryParams: { pageNo: 0, pageSize: 25 } });
      },
      error: (error) => {
        this.showLoader = false;
        this.router.navigateByUrl("/");
        this.toastrService.error(error.error, "", { progressBar: true });
      },
      complete: () => {
        this.toastrService.success("Books successfully deleted", "", { progressBar: true });
      }
    });
  }

  onCancel(): void {
    this.router.navigate(['/books'], { queryParams: { pageNo: 0, pageSize: 25 } });
  }

  isAdmin(): boolean {
    return this.roles.includes("ROLE_ADMIN");
  }
}
