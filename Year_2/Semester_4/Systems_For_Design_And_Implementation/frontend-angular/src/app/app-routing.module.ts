import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { BookComponent } from './components/book/book.component';
import { LibraryComponent } from './components/library/library.component';
import { ReaderComponent } from './components/reader/reader.component';
import { MainPageComponent } from './components/main-page/main-page.component';
import { LibraryDetailsComponent } from './components/library/library-details/library-details.component';
import { BookDetailsComponent } from './components/book/book-details/book-details.component';
import { ReaderDetailsComponent } from './components/reader/reader-details/reader-details.component';
import { BookFilterComponent } from './components/book/book-filter/book-filter.component';
import { LibraryBooksStatisticsComponent } from './components/library/library-books-statistics/library-books-statistics.component';
import { LibraryReadersStatisticsComponent } from './components/library/library-readers-statistics/library-readers-statistics.component';
import { LibraryAddComponent } from './components/library/library-add/library-add.component';
import { LibraryUpdateComponent } from './components/library/library-update/library-update.component';
import { LibraryDeleteComponent } from './components/library/library-delete/library-delete.component';
import { BookUpdateComponent } from './components/book/book-update/book-update.component';
import { BookDeleteComponent } from './components/book/book-delete/book-delete.component';
import { ReaderUpdateComponent } from './components/reader/reader-update/reader-update.component';
import { ReaderDeleteComponent } from './components/reader/reader-delete/reader-delete.component';
import { ReaderAddComponent } from './components/reader/reader-add/reader-add.component';
import { BookAddComponent } from './components/book/book-add/book-add.component';
import { ReaderNewMembershipComponent } from './components/reader/reader-new-membership/reader-new-membership.component';
import { LoginComponent } from './components/registration/login/login.component';
import { UserProfileComponent } from './components/user/user-profile/user-profile.component';
import { RegisterComponent } from './components/registration/register/register.component';
import { RegisterConfirmationComponent } from './components/registration/register-confirmation/register-confirmation.component';
import { CurrentUserProfileComponent } from './components/user/current-user-profile/current-user-profile.component';
import { UpdateUserProfileComponent } from './components/user/update-user-profile/update-user-profile.component';
import { AdminRolesDashboardComponent } from './components/user/admin-roles-dashboard/admin-roles-dashboard.component';
import { AdminDataManagementDashboardComponent } from './components/user/admin-data-management-dashboard/admin-data-management-dashboard.component';

const routes: Routes = [
  {
    path: '',
    component: MainPageComponent
  },
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'register',
    component: RegisterComponent
  },
  {
    path: 'register-confirmation/:code',
    component: RegisterConfirmationComponent
  },
  {
    path: 'users/:username',
    component: UserProfileComponent
  },
  {
    path: 'users-update/:username',
    component: UpdateUserProfileComponent
  },
  {
    path: 'users-active/:username',
    component: CurrentUserProfileComponent
  },
  {
    path: 'admin-roles-dashboard',
    component: AdminRolesDashboardComponent
  },
  {
    path: 'admin-data-management-dashboard',
    component: AdminDataManagementDashboardComponent
  },
  {
    path: 'books',
    component: BookComponent,
    data: { pageNo: 0, pageSize: 25 }
  },
  {
    path: 'books-filter',
    component: BookFilterComponent,
    data: { minPrice: 0, pageNo: 0, pageSize: 25 }
  },
  {
    path: 'books/:id',
    component: BookDetailsComponent
  },
  {
    path: 'books-add',
    component: BookAddComponent
  },
  {
    path: 'books-update/:id',
    component: BookUpdateComponent
  },
  {
    path: 'books-delete/:id',
    component: BookDeleteComponent
  },
  {
    path: 'libraries',
    component: LibraryComponent,
    data: { pageNo: 0, pageSize: 25 }
  },
  {
    path: 'libraries/:id',
    component: LibraryDetailsComponent
  },
  {
    path: 'libraries-books-statistics',
    component: LibraryBooksStatisticsComponent
  },
  {
    path: 'libraries-readers-statistics',
    component: LibraryReadersStatisticsComponent
  },
  {
    path: 'libraries-add',
    component: LibraryAddComponent
  },
  {
    path: 'libraries-update/:id',
    component: LibraryUpdateComponent
  },
  {
    path: 'libraries-delete/:id',
    component: LibraryDeleteComponent
  },
  {
    path: 'readers',
    component: ReaderComponent,
    data: { pageNo: 0, pageSize: 25 }
  },
  {
    path: 'readers/:id',
    component: ReaderDetailsComponent
  },
  {
    path: 'reader-membership/:id',
    component: ReaderNewMembershipComponent
  },
  {
    path: 'readers-add',
    component: ReaderAddComponent
  },
  {
    path: 'readers-update/:id',
    component: ReaderUpdateComponent
  },
  {
    path: 'readers-delete/:id',
    component: ReaderDeleteComponent
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})

export class AppRoutingModule { }
