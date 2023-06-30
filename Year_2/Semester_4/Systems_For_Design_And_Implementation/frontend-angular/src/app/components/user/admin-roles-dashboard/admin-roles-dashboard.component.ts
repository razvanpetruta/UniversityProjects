import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Subject, debounceTime } from 'rxjs';
import { Library } from 'src/app/core/model/library.model';
import { Membership, ReaderDetails } from 'src/app/core/model/reader.model';
import { UserProfile, UserRolesUpdate, UserWithRolesDTO } from 'src/app/core/model/user.model';
import { StorageService } from 'src/app/core/service/_services/storage.service';
import { UserService } from 'src/app/core/service/_services/user.service';
import { LibraryService } from 'src/app/core/service/library.service';
import { ReaderService } from 'src/app/core/service/reader.service';

@Component({
  selector: 'app-admin-roles-dashboard',
  templateUrl: './admin-roles-dashboard.component.html',
  styleUrls: ['./admin-roles-dashboard.component.css']
})
export class AdminRolesDashboardComponent implements OnInit {
  showSearchLoader: boolean = false;
  showLoader: boolean = false;

  selectedOption?: string;
  selectedUser?: UserWithRolesDTO;
  selectedUserRoles?: [
    {
      name: string;
      id: number;
    }
  ];
  searchTerm = new Subject<string>();
  options?: UserWithRolesDTO[];
  user?: UserWithRolesDTO;
  checkAdmin?: boolean;
  checkUser?: boolean;
  checkModerator?: boolean;

  selectedElementsPerPage?: number;

  roles: string[] = [];
  isLoggedIn = false;
  username?: string;

  constructor(
    private userService: UserService, 
    private router: Router, 
    private toastrService: ToastrService,
    private storageService: StorageService) { }

  ngOnInit(): void {
    this.isLoggedIn = this.storageService.isLoggedIn();

    if (this.isLoggedIn) {
      const user = this.storageService.getUser();
      this.roles = user.roles;

      this.username = user.username;
      if (!this.isAdmin()) {
        this.toastrService.error("You are not an admin", "", { progressBar: true });
        this.router.navigateByUrl("/");
      }
    } else {
      this.toastrService.error("Log in is required", "", { progressBar: true });
      this.router.navigateByUrl("/login");
    }

    this.userService.getElementsPerPage().subscribe({
      next: (response) => {
        this.selectedElementsPerPage = response;
      }
    });

    this.searchTerm.pipe(
      debounceTime(1000) // debounce by 1 second
    ).subscribe(term => {
      this.showSearchLoader = true;

      if (term.trim()) {
        this.userService.getUsersByUsername(term).subscribe({
          next: (users: UserWithRolesDTO[]) => {
            this.options = users;
          },
          error: (error) => {
            this.showLoader = false;
            this.router.navigateByUrl("/");
            this.toastrService.error(error.error, "", { progressBar: true });
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

  onSubmit(form: any): void {
    this.showLoader = true;
  }

  onSelection(event: any): void { 
    this.selectedOption = event.option.value.username;
    this.selectedUser = event.option.value;

    this.userService.getUserProfile(this.selectedUser!.username).subscribe({
      next: (user: UserProfile) => {
        this.selectedUserRoles = user.roles;
        this.checkUser = this.isSelectedUser();
        this.checkModerator = this.isSelectedModerator();
        this.checkAdmin = this.isSelectedAdmin();
      },
      error: (error) => {

      },
      complete: () => {

      }
    });
  }

  updateRoles(): void {
    const roles = [];

    if (this.checkUser) {
      roles.push("ROLE_USER");
    }

    if (this.checkModerator) {
      roles.push("ROLE_MODERATOR");
    }

    if (this.checkAdmin) {
      roles.push("ROLE_ADMIN");
    }

    const userRoles: UserRolesUpdate = {
      roles: roles
    }

    this.userService.updateUserRoles(this.selectedUser!.username, userRoles).subscribe({
      next: (user: UserWithRolesDTO) => {
        this.selectedUser = user;
        this.selectedUserRoles = user.roles;
        this.checkUser = this.isSelectedUser();
        this.checkModerator = this.isSelectedModerator();
        this.checkAdmin = this.isSelectedAdmin();
      },
      error: (error) => {

      },
      complete: () => {
        this.toastrService.success("Roles updated successfully", "", { progressBar: true });
      }
    });
  }

  search(term: string): void {
    this.searchTerm.next(term);
  }

  isModerator(): boolean {
    return this.roles.includes("ROLE_MODERATOR");
  }

  isAdmin(): boolean {
    return this.roles.includes("ROLE_ADMIN");
  }

  isSelectedUser(): boolean {
    if (!this.selectedUserRoles) {
      return false;
    }

    let found = false;

    this.selectedUserRoles.forEach((role) => {
      if (role.name === "ROLE_USER") {
        found = true;
      }
    });

    return found;
  }

  isSelectedModerator(): boolean {
    if (!this.selectedUserRoles) {
      return false;
    }

    let found = false;

    this.selectedUserRoles.forEach((role) => {
      if (role.name === "ROLE_MODERATOR") {
        found = true;
      }
    });

    return found;
  }

  isSelectedAdmin(): boolean {
    if (!this.selectedUserRoles) {
      return false;
    }

    let found = false;

    this.selectedUserRoles.forEach((role) => {
      if (role.name === "ROLE_ADMIN") {
        found = true;
      }
    });

    return found;
  }

  updateElementsPerPage(): void {
    if (!this.selectedElementsPerPage) {
      return;
    }

    this.userService.updateElementsPerPage(this.selectedElementsPerPage).subscribe({
      next: (response) => {
        this.showLoader = false;
      },
      complete: () => {
        this.toastrService.success("Number of elements per page successfully updated updated successfully", "", { progressBar: true });
      }
    });
  }
}
