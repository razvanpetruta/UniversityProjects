import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { BookDetails } from 'src/app/core/model/book.model';
import { StorageService } from 'src/app/core/service/_services/storage.service';
import { BookService } from 'src/app/core/service/book.service';

@Component({
  selector: 'app-book-details',
  templateUrl: './book-details.component.html',
  styleUrls: ['./book-details.component.css']
})
export class BookDetailsComponent implements OnInit {
  book?: BookDetails;
  bookID?: string;
  showLoader: boolean = false;

  roles: string[] = [];
  isLoggedIn = false;
  username?: string;

  constructor(
    private bookService: BookService,
    private activatedRoute: ActivatedRoute,
    private storageService: StorageService,
    private toastrService: ToastrService,
    private router: Router) {}

  ngOnInit(): void {
    this.isLoggedIn = this.storageService.isLoggedIn();

    if (this.isLoggedIn) {
      const user = this.storageService.getUser();
      this.roles = user.roles;

      this.username = user.username;
    }

    this.showLoader = true;

    this.activatedRoute.params.subscribe(params => {
      this.bookID = params['id'];
      this.bookService.getBook(this.bookID!).subscribe({
        next: (book: BookDetails) => {
          this.book = book;
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

  isUserCorrect(): boolean {
    if (!this.book) {
      return false;
    }

    return this.username === this.book.username;
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
