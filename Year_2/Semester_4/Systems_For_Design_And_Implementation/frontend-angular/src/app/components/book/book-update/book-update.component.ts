import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Book, BookDetails, UpdateBookDTO } from 'src/app/core/model/book.model';
import { StorageService } from 'src/app/core/service/_services/storage.service';
import { BookService } from 'src/app/core/service/book.service';

@Component({
  selector: 'app-book-update',
  templateUrl: './book-update.component.html',
  styleUrls: ['./book-update.component.css']
})
export class BookUpdateComponent implements OnInit {
  bookID?: string;
  title?: string;
  author?: string;
  publisher?: string;
  price?: number;
  publishedYear?: number;
  description?: string;
  showLoader: boolean = false;

  roles: string[] = [];
  isLoggedIn = false;
  username?: string;

  constructor(
    private bookService: BookService, 
    private activatedRoute: ActivatedRoute, 
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

    this.showLoader = true;

    this.activatedRoute.params.subscribe(params => {
      this.bookID = params['id'];
      this.bookService.getBook(this.bookID!).subscribe({
        next: (book: BookDetails) => {
          this.title = book.title;
          this.author = book.author;
          this.publisher = book.publisher;
          this.price = book.price;
          this.publishedYear = book.publishedYear;
          this.description = book.description;
          if (this.username !== book.username && !this.isModerator() && !this.isAdmin()) {
            this.toastrService.error("You are not allowed to update this book", "", { progressBar: true });
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
    if (this.title && this.author && this.publisher && this.description && this.publishedYear && this.description && this.price) {
      const book: UpdateBookDTO = {
        title: this.title,
        author: this.author,
        price: this.price,
        description: this.description,
        publisher: this.publisher,
        publishedYear: this.publishedYear
      }
      
      this.bookService.updateBook(this.bookID!, book).subscribe({
        next: (book: Book) => {
          this.router.navigateByUrl("/books/" + book.id);
        },
        error: (error) => {
          this.toastrService.error(error.error, "", { progressBar: true });
        },
        complete: () => {
          this.toastrService.success("Book successfully updated", "", { progressBar: true });
        }
      });
    }
  }

  isModerator(): boolean {
    return this.roles.includes("ROLE_MODERATOR");
  }

  isAdmin(): boolean {
    return this.roles.includes("ROLE_ADMIN");
  }
}
