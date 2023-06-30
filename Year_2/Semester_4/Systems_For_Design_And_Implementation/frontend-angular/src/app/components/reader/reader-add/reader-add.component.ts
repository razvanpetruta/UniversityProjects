import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { AddUpdateReaderDTO, Reader } from 'src/app/core/model/reader.model';
import { StorageService } from 'src/app/core/service/_services/storage.service';
import { ReaderService } from 'src/app/core/service/reader.service';

@Component({
  selector: 'app-reader-add',
  templateUrl: './reader-add.component.html',
  styleUrls: ['./reader-add.component.css']
})
export class ReaderAddComponent implements OnInit {
  name?: string;
  email?: string;
  birthDate?: Date;
  gender?: string;
  student?: boolean;
  showLoader: boolean = false;
  yesterdayDate?: string;
  errorMessages = {
    'name': null,
    'email': null,
    'birthDate': null,
    'gender': null,
    'student': null
  };

  roles: string[] = [];
  isLoggedIn = false;
  username?: string;

  constructor(
    private readerService: ReaderService, 
    private toastrService: ToastrService,
    private router: Router,
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

    const today = new Date();
    const yesterday = new Date(today.getTime() - (24 * 60 * 60 * 1000));
    this.yesterdayDate = yesterday.toISOString().slice(0, 10);
  }

  onSubmit(form: any): void {
    this.showLoader = true;

    if (this.name && this.email && this.gender && this.birthDate) {
      if (this.student === undefined) {
        this.student = false;
      }

      const reader: AddUpdateReaderDTO = {
        name: this.name,
        email: this.email,
        birthDate: this.birthDate,
        gender: this.gender,
        student: this.student
      }
      
      this.readerService.addReader(reader).subscribe({
        next: (reader: Reader) => {
          this.router.navigateByUrl("/readers/" + reader.id);
        },
        error: (error) => {
          this.showLoader = false;

          if (error.status === 400) {
            const errors = error.error.errors;

            if ('name' in errors) {
              this.errorMessages.name = errors.name;
            } else {
              this.errorMessages.name = null;
            }

            if ('email' in errors) {
              this.errorMessages.email = errors.email;
            } else {
              this.errorMessages.email = null;
            }

            if ('gender' in errors) {
              this.errorMessages.gender = errors.gender;
            } else {
              this.errorMessages.gender = null;
            }

            if ('birthDate' in errors) {
              this.errorMessages.birthDate = errors.birthDate;
            } else {
              this.errorMessages.birthDate = null;
            }

            if ('student' in errors) {
              this.errorMessages.student = errors.student;
            } else {
              this.errorMessages.student = null;
            }
          }
        },
        complete: () => {
          this.showLoader = false;
          this.toastrService.success("Reader added successfully", "", { progressBar: true });
        }
      });
    }
  }
}
