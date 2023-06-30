import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator, MatPaginatorIntl, PageEvent } from '@angular/material/paginator';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Subject, debounceTime } from 'rxjs';
import { Book } from 'src/app/core/model/book.model';
import { BookService } from 'src/app/core/service/book.service';

@Component({
  selector: 'app-book-filter',
  templateUrl: './book-filter.component.html',
  styleUrls: ['./book-filter.component.css']
})
export class BookFilterComponent implements OnInit {
  @ViewChild(MatPaginator) paginator?: MatPaginator;

  books: Book[] = [];

  pageNumber: number = 0;
  pageSize: number = 25; 
  noPages: number = 0;
  goToPageNumber: number = 1;

  selectedPrice?: string;
  searchTerm = new Subject<string>();
  showLoader: boolean = false;
  showPaginator: boolean = false;

  constructor(
    private bookService: BookService, 
    private route: ActivatedRoute, 
    private router: Router, 
    private paginatorIntl: MatPaginatorIntl,
    private toastrService: ToastrService) {}
  
  ngOnInit(): void {
    // customize paginator
    this.paginatorIntl.getRangeLabel = this.getRangeLabel.bind(this.paginatorIntl);

    this.searchTerm.pipe(
      debounceTime(1000) // debounce by 1 second
    ).subscribe(term => {
      this.pageNumber = 0;
      this.goToPageNumber = 1;
      this.showPaginator = true;
      this.listBooks();
    });

    this.route.queryParams.subscribe(params => {
      this.pageNumber = Number(params['pageNo']) || 0;
      this.pageSize = Number(params['pageSize']) || 25;
    });
  }

  listBooks(): void {
    this.showLoader = true;

    this.bookService.countBooksWithMinPrice(this.selectedPrice!).subscribe((result: Number) => {
      this.noPages = Math.floor(result.valueOf() / this.pageSize);
      if (result.valueOf() % this.pageSize > 0) {
        this.noPages++;
      }
    });

    this.bookService.getBookWithMinPrice(this.selectedPrice!, this.pageNumber, this.pageSize).subscribe({
      next: (result: Book[]) => {
        this.books = result;
      },
      error: (error) => {
        this.showLoader = false;
        this.router.navigateByUrl("/");
        this.toastrService.error(error.error, "", { progressBar: true });
      },
      complete: () => {
        this.router.navigate(['/books-filter'], { queryParams: { minPrice: this.selectedPrice, pageNo: this.pageNumber, pageSize: this.pageSize } });
        this.showLoader = false;
      }
    });
  }

  search(term: string): void {
    if (this.selectedPrice === "") {
      return;
    }

    this.searchTerm.next(term);
  }

  onPageChanged(event: PageEvent) {
    this.pageNumber = event.pageIndex;
    this.goToPageNumber = this.pageNumber + 1;
    this.pageSize = event.pageSize;
    
    this.bookService.countBooksWithMinPrice(this.selectedPrice!).subscribe((result: Number) => {
      this.noPages = Math.floor(result.valueOf() / this.pageSize);
      if (result.valueOf() % this.pageSize > 0) {
        this.noPages++;
      }
    });

    this.router.navigate(['/books-filter'], { queryParams: { minPrice: this.selectedPrice, pageNo: this.pageNumber, pageSize: this.pageSize } })
    .then(() => this.listBooks());
  }

  goToPage(): void {
    this.pageNumber = Math.min(Math.max(1, this.goToPageNumber), this.noPages) - 1;
    this.router.navigate(['/books-filter'], { queryParams: { minPrice: this.selectedPrice, pageNo: this.pageNumber, pageSize: this.pageSize } })
        .then(() => this.listBooks());
  }

  checkPageNumber(): void {
    if (this.goToPageNumber > this.noPages) {
      this.goToPageNumber = this.noPages;
    }
  }

  getRangeLabel(page: number, pageSize: number, length: number): string {
    const total = Math.ceil(length / pageSize);
    return `Page ${page + 1} of ${total}`;
  }
}
