import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { LibraryCount } from 'src/app/core/model/library.model';
import { LibraryService } from 'src/app/core/service/library.service';

@Component({
  selector: 'app-library-readers-statistics',
  templateUrl: './library-readers-statistics.component.html',
  styleUrls: ['./library-readers-statistics.component.css']
})
export class LibraryReadersStatisticsComponent implements OnInit {
  libraries: LibraryCount[] = [];
  showLoader: boolean = true;

  constructor(
    private libraryService: LibraryService,
    private toastrService: ToastrService,
    private router: Router) {}
  
  ngOnInit(): void {
    this.showLoader = true;

    this.libraryService.getReadersStatistics().subscribe({
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
