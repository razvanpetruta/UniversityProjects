import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { ReaderDetails } from 'src/app/core/model/reader.model';
import { StorageService } from 'src/app/core/service/_services/storage.service';
import { ReaderService } from 'src/app/core/service/reader.service';

@Component({
  selector: 'app-reader-details',
  templateUrl: './reader-details.component.html',
  styleUrls: ['./reader-details.component.css']
})
export class ReaderDetailsComponent implements OnInit{
  reader?: ReaderDetails;
  readerID?: string;
  showLoader: boolean = false;

  roles: string[] = [];
  isLoggedIn = false;
  username?: string;

  constructor(
    private readerService: ReaderService, 
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
      this.readerID = params['id'];
      this.readerService.getReader(this.readerID!).subscribe({
        next: (reader: ReaderDetails) => {
          this.reader = reader;
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

  isActive(date: Date): boolean {
    return !(new Date(date).getTime() < new Date().getTime());
  }

  isUser(): boolean {
    return this.roles.includes("ROLE_USER");
  }

  isUserCorrect(): boolean {
    if (!this.reader) {
      return false;
    }

    return this.username === this.reader.username;
  }

  isModerator(): boolean {
    return this.roles.includes("ROLE_MODERATOR");
  }

  isAdmin(): boolean {
    return this.roles.includes("ROLE_ADMIN");
  }
}
