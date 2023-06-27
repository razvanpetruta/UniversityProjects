import { Component, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { Book, RentedBook } from 'src/app/core/model/book.model';
import { GeneralService } from 'src/app/core/service/general.service';
import { StorageService } from 'src/app/core/service/storage.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'],
})
export class DashboardComponent implements OnInit {
  genres: string[] = [];
  books: Book[] = [];
  rentedBooks: RentedBook[] = [];
  unapprovedRentals: RentedBook[] = [];
  selectedGenre: string = '';

  role: string = '';
  isLoggedIn = false;
  username: string = '';

  constructor(
    private generalService: GeneralService,
    private storageService: StorageService,
    private toastrService: ToastrService
  ) {}

  ngOnInit(): void {
    this.isLoggedIn = this.storageService.isLoggedIn();

    if (this.isLoggedIn) {
      const user = this.storageService.getUser();
      this.role = user.role;
      this.username = user.username;
    }

    this.generalService.getGenres().subscribe({
      next: (response) => {
        this.genres = response;
      },
      complete: () => {
        this.selectedGenre = this.genres[0];
        this.onGenreSelect();
      },
    });

    this.getRentals();

    this.getUnapprovedRentals();
  }

  getRentals(): void {
    const user = this.storageService.getUser();
    this.generalService.getRentals(user.username).subscribe({
      next: (response) => {
        if (response.value.length > 0) {
          this.rentedBooks = response.value;
        } else {
          this.rentedBooks = [];
        }
      },
    });
  }

  getUnapprovedRentals(): void {
    this.generalService.getUnapprovedRentals().subscribe({
      next: (response) => {
        if (response.value.length > 0) {
          this.unapprovedRentals = response.value;
        } else {
          this.unapprovedRentals = [];
        }
      },
    });
  }

  onGenreSelect() {
    this.generalService.getFilteredBooks(this.selectedGenre).subscribe({
      next: (response) => {
        this.books = response;
      },
    });
  }

  requestBook(id: number): void {
    this.generalService.requestBook(id).subscribe({
      next: (response) => {
        if (response.statusCode != 200) {
          this.toastrService.error(response.value, '', {
            progressBar: true,
          });
        } else {
          this.toastrService.success('Successfully requested', '', {
            progressBar: true,
          });
        }
      },
      error: (error) => {
        console.log(error);
      },
    });
  }

  approveRental(id: number): void {
    const user = this.storageService.getUser();
    this.generalService.approveRental(id).subscribe({
      next: (response) => {
        if (response.statusCode != 200) {
          this.toastrService.error(response.value, '', {
            progressBar: true,
          });
        } else {
          this.toastrService.success('Successfully approved', '', {
            progressBar: true,
          });
        }
      },
      error: (error) => {},
      complete: () => {
        this.getRentals();
        this.getUnapprovedRentals();
      },
    });
  }

  rejectRental(id: number): void {
    const user = this.storageService.getUser();
    this.generalService.rejectRental(id).subscribe({
      next: (response) => {
        this.toastrService.success('Successfully rejected', '', {
          progressBar: true,
        });
      },
      error: (error) => {},
      complete: () => {
        this.getRentals();
        this.getUnapprovedRentals();
      },
    });
  }

  deleteBook(id: number): void {
    const user = this.storageService.getUser();
    if (confirm('Are you sure you want to delete this book?')) {
      this.generalService.deleteBook(id).subscribe({
        next: (response) => {
          this.toastrService.success('Successfully deleted', '', {
            progressBar: true,
          });
        },
        complete: () => {
          this.onGenreSelect();
        },
      });
    }
  }

  isUser(): boolean {
    return this.role === 'USER';
  }

  isAdmin(): boolean {
    return this.role === 'ADMIN';
  }
}
