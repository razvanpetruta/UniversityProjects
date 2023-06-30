import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { LibraryCount } from 'src/app/core/model/library.model';
import { LibraryService } from 'src/app/core/service/library.service';

@Component({
  selector: 'app-library-books-statistics',
  templateUrl: './library-books-statistics.component.html',
  styleUrls: ['./library-books-statistics.component.css']
})
export class LibraryBooksStatisticsComponent implements OnInit {
  libraries: LibraryCount[] = [];
  showLoader: boolean = false;

  constructor(
    private libraryService: LibraryService,
    private toastrService: ToastrService,
    private router: Router) {}
  
  ngOnInit(): void {
    this.showLoader = true;

    this.libraryService.getBooksStatistics().subscribe({
      next: (result: LibraryCount[]) => {
        this.libraries = result;
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
  }
}
