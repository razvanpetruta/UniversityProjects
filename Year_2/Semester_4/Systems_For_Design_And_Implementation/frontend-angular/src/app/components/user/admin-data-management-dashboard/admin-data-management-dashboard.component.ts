import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { SQLResponse } from 'src/app/core/model/user.model';
import { StorageService } from 'src/app/core/service/_services/storage.service';
import { UserService } from 'src/app/core/service/_services/user.service';

@Component({
  selector: 'app-admin-data-management-dashboard',
  templateUrl: './admin-data-management-dashboard.component.html',
  styleUrls: ['./admin-data-management-dashboard.component.css'],
})
export class AdminDataManagementDashboardComponent implements OnInit {
  roles: string[] = [];
  isLoggedIn = false;
  username?: string;
  messages: string[] = [];

  constructor(
    private userService: UserService,
    private router: Router,
    private toastrService: ToastrService,
    private storageService: StorageService
  ) {}

  ngOnInit(): void {
    this.isLoggedIn = this.storageService.isLoggedIn();

    if (this.isLoggedIn) {
      const user = this.storageService.getUser();
      this.roles = user.roles;

      this.username = user.username;
      if (!this.isAdmin()) {
        this.toastrService.error('You are not an admin', '', {
          progressBar: true,
        });
        this.router.navigateByUrl('/');
      }
    } else {
      this.toastrService.error('Log in is required', '', { progressBar: true });
      this.router.navigateByUrl('/login');
    }
  }

  isAdmin(): boolean {
    return this.roles.includes('ROLE_ADMIN');
  }

  deleteLibraries() {
    this.messages.push('Deleting libraries...');
    this.userService.deleteAllLibraries().subscribe({
      next: (message: SQLResponse) => {
        this.messages.push(message.message);
      },
      error: (error) => {
        console.log(error);
      },
      complete: () => {},
    });
  }

  insertLibraries() {
    this.messages.push('Inserting libraries...');
    this.userService.insertAllLibraries().subscribe({
      next: (message: SQLResponse) => {
        this.messages.push(message.message);
      },
      error: (error) => {},
      complete: () => {},
    });
  }

  deleteBooks() {
    this.messages.push('Deleting books...');
    this.userService.deleteAllBooks().subscribe({
      next: (message: SQLResponse) => {
        this.messages.push(message.message);
      },
      error: (error) => {},
      complete: () => {},
    });
  }

  insertBooks() {
    this.messages.push('Inserting books...');
    this.userService.insertAllBooks().subscribe({
      next: (message: SQLResponse) => {
        this.messages.push(message.message);
      },
      error: (error) => {},
      complete: () => {},
    });
  }

  deleteMemberships() {
    this.messages.push('Deleting memberships...');
    this.userService.deleteAllMemberships().subscribe({
      next: (message: SQLResponse) => {
        this.messages.push(message.message);
      },
      error: (error) => {},
      complete: () => {},
    });
  }

  insertMemberships() {
    this.messages.push('Inserting memberships...');
    this.userService.insertAllMemberships().subscribe({
      next: (message: SQLResponse) => {
        this.messages.push(message.message);
      },
      error: (error) => {},
      complete: () => {},
    });
  }

  deleteReaders() {
    this.messages.push('Deleting readers...');
    this.userService.deleteAllReaders().subscribe({
      next: (message: SQLResponse) => {
        this.messages.push(message.message);
      },
      error: (error) => {
        console.log(error);
      },
      complete: () => {},
    });
  }

  insertReaders() {
    this.messages.push('Inserting readers...');
    this.userService.insertAllReaders().subscribe({
      next: (message: SQLResponse) => {
        this.messages.push(message.message);
      },
      error: (error) => {},
      complete: () => {},
    });
  }

  deleteAll() {
    this.messages.push('Deleting memberships...');
    this.userService.deleteAllMemberships().subscribe({
      next: (message: SQLResponse) => {
        this.messages.push(message.message);
      },
      error: (error) => {},
      complete: () => {
        this.messages.push('Deleting readers...');
        this.userService.deleteAllReaders().subscribe({
          next: (message: SQLResponse) => {
            this.messages.push(message.message);
          },
          error: (error) => {
            console.log(error);
          },
          complete: () => {
            this.messages.push('Deleting books...');
            this.userService.deleteAllBooks().subscribe({
              next: (message: SQLResponse) => {
                this.messages.push(message.message);
              },
              error: (error) => {},
              complete: () => {
                this.messages.push('Deleting libraries...');
                this.userService.deleteAllLibraries().subscribe({
                  next: (message: SQLResponse) => {
                    this.messages.push(message.message);
                  },
                  error: (error) => {
                    console.log(error);
                  },
                  complete: () => {},
                });
              },
            });
          },
        });
      },
    });
  }

  insertAll() {
    this.messages.push('Inserting libraries...');
    this.userService.insertAllLibraries().subscribe({
      next: (message: SQLResponse) => {
        this.messages.push(message.message);
      },
      error: (error) => {},
      complete: () => {
        this.messages.push('Inserting books...');
        this.userService.insertAllBooks().subscribe({
          next: (message: SQLResponse) => {
            this.messages.push(message.message);
          },
          error: (error) => {},
          complete: () => {
            this.messages.push('Inserting readers...');
            this.userService.insertAllReaders().subscribe({
              next: (message: SQLResponse) => {
                this.messages.push(message.message);
              },
              error: (error) => {},
              complete: () => {
                this.messages.push('Inserting memberships...');
                this.userService.insertAllMemberships().subscribe({
                  next: (message: SQLResponse) => {
                    this.messages.push(message.message);
                  },
                  error: (error) => {},
                  complete: () => {},
                });
              },
            });
          },
        });
      },
    });
  }

  clearConsole() {
    this.messages = [];
  }
}
