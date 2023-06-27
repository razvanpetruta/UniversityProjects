import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { GeneralService } from 'src/app/core/service/general.service';
import { StorageService } from 'src/app/core/service/storage.service';

@Component({
  selector: 'app-book-update',
  templateUrl: './book-update.component.html',
  styleUrls: ['./book-update.component.css']
})
export class BookUpdateComponent {
  role?: string;
  isLoggedIn = false;
  username?: string;

  title?: string;
  author?: string;
  noPages?: number;
  price?: number;
  genre?: string;
  bookID?: number;

  constructor(
    private router: Router, 
    private toastrService: ToastrService,
    private storageService: StorageService,
    private activatedRoute: ActivatedRoute,
    private generalService: GeneralService) {}

  ngOnInit(): void {
    this.isLoggedIn = this.storageService.isLoggedIn();

    if (this.isLoggedIn) {
      const user = this.storageService.getUser();
      this.role = user.role;

      this.username = user.user;
    } else {
      this.toastrService.error("Log in required", "", { progressBar: true });
      this.router.navigateByUrl("/login");
    }

    this.activatedRoute.params.subscribe(params => {
      this.bookID = params['id'];
      this.generalService.getBookByID(this.bookID!).subscribe({
        next: (response) => {
          this.title = response.title;
          this.author = response.author;
          this.noPages = response.noPages;
          this.price = response.price;
          this.genre = response.genre;
        }
      });
    });
  }

  onSubmit(form: any) {
    if (this.username && this.role && this.bookID && this.title && this.author && this.noPages && this.price && this.genre) {
      if (this.title.length < 3) {
        this.toastrService.error("Title must have at least 3 characters", "", { progressBar: true });
        return;
      }

      if (this.author.length < 3) {
        this.toastrService.error("Author must have at least 3 characters", "", { progressBar: true });
        return;
      }

      if (this.noPages < 5 || this.noPages > 10000) {
        this.toastrService.error("Number of pages must be between 5 and 10 000", "", { progressBar: true });
        return;
      }

      if (this.price > 1000) {
        this.toastrService.error("Price must be smaller than 1000", "", { progressBar: true });
        return;
      }

      const user = this.storageService.getUser();
      this.generalService.updateBook(user.session_id, this.bookID, this.title, this.author, this.noPages, this.price, this.genre).subscribe({
        next: (response) => {
          this.toastrService.success(response.message, "", { progressBar: true });
        },
        complete: () => {
          this.router.navigateByUrl("/dashboard");   
        }
      });
    }
  }
}
