import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Subject, debounceTime } from 'rxjs';
import { AddBookDTO, Book } from 'src/app/core/model/book.model';
import { Library } from 'src/app/core/model/library.model';
import { StorageService } from 'src/app/core/service/_services/storage.service';
import { BookService } from 'src/app/core/service/book.service';
import { LibraryService } from 'src/app/core/service/library.service';

@Component({
  selector: 'app-book-add',
  templateUrl: './book-add.component.html',
  styleUrls: ['./book-add.component.css']
})
export class BookAddComponent implements OnInit {
  title?: string;
  author?: string;
  publisher?: string;
  price?: number;
  publishedYear?: number;
  libraryID?: string;
  description?: string;

  showLoader: boolean = false;
  showSearchLoader: boolean = false;

  errorMessages = {
    'title': null,
    'author': null,
    'description': null,
    'publisher': null,
    'publishedYear': null,
    'price': null
  };

  selectedOption?: string;
  selectedLibrary?: Library;
  searchTerm = new Subject<string>();
  options?: Library[];

  roles: string[] = [];
  isLoggedIn = false;
  username?: string;

  constructor(
    private bookService: BookService, 
    private libraryService: LibraryService, 
    private router: Router,
    private storageService: StorageService,
    private toastrService: ToastrService) {}

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

    this.searchTerm.pipe(
      debounceTime(1000) // debounce by 1 second
    ).subscribe(term => {
      this.showSearchLoader = true;

      if (term.trim()) {
        this.libraryService.getLibrariesByName(term).subscribe({
          next: (libraries: Library[]) => {
            this.options = libraries;
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

  onSubmit(form: any) {
    this.showLoader = true;

    if (this.title && this.author && this.description && this.price && this.publishedYear && this.publisher) {
      const book: AddBookDTO = {
        title: this.title,
        author: this.author,
        publisher: this.publisher,
        price: this.price,
        publishedYear: this.publishedYear,
        description: this.description
      }

      this.libraryID = this.selectedLibrary?.id.toString();
      
      this.bookService.addBook(book, this.libraryID!).subscribe({
        next: (book: Book) => {
          this.router.navigateByUrl("/books/" + book.id);
        },
        error: (error) => {
          this.showLoader = false;

          if (error.status === 400) {
            const errors = error.error.errors;

            if ('title' in errors) {
              this.errorMessages.title = errors.title;
            } else {
              this.errorMessages.title = null;
            }

            if ('author' in errors) {
              this.errorMessages.author = errors.author;
            } else {
              this.errorMessages.author = null;
            }

            if ('description' in errors) {
              this.errorMessages.description = errors.description;
            } else {
              this.errorMessages.description = null;
            }

            if ('publisher' in errors) {
              this.errorMessages.publisher = errors.publisher;
            } else {
              this.errorMessages.publisher = null;
            }

            if ('publishedYear' in errors) {
              this.errorMessages.publishedYear = errors.publishedYear;
            } else {
              this.errorMessages.publishedYear = null;
            }

            if ('price' in errors) {
              this.errorMessages.price = errors.price;
            } else {
              this.errorMessages.price = null;
            }
          }
        },
        complete: () => {
          this.showLoader = false;
          this.toastrService.success("Book added successfully", "", { progressBar: true });
        }
      })
    }
  }

  onSelection(event: any): void {
    this.selectedOption = event.option.value.name;
    this.selectedLibrary = event.option.value;
  }

  search(term: string): void {
    this.searchTerm.next(term);
  }
}
