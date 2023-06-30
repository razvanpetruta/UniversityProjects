import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatInputModule } from '@angular/material/input';
import { MatPaginatorModule } from '@angular/material/paginator';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BookComponent } from './components/book/book.component';
import { LibraryComponent } from './components/library/library.component';
import { ReaderComponent } from './components/reader/reader.component';
import { NavbarComponent } from './shared/navbar/navbar.component';
import { FooterComponent } from './shared/footer/footer.component';
import { MainPageComponent } from './components/main-page/main-page.component';
import { LibraryService } from './core/service/library.service';
import { BookService } from './core/service/book.service';
import { LibraryDetailsComponent } from './components/library/library-details/library-details.component';
import { BookDetailsComponent } from './components/book/book-details/book-details.component';
import { ReaderDetailsComponent } from './components/reader/reader-details/reader-details.component';
import { BookFilterComponent } from './components/book/book-filter/book-filter.component';
import { FormsModule } from '@angular/forms';
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
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { BookAddComponent } from './components/book/book-add/book-add.component';
import { ReaderNewMembershipComponent } from './components/reader/reader-new-membership/reader-new-membership.component';
import { LoginComponent } from './components/registration/login/login.component';
import { ToastrModule } from 'ngx-toastr';

import { httpInterceptorProviders } from './core/service/_helpers/http.interceptor';
import { UserProfileComponent } from './components/user/user-profile/user-profile.component';
import { RegisterComponent } from './components/registration/register/register.component';
import { RegisterConfirmationComponent } from './components/registration/register-confirmation/register-confirmation.component';
import { CurrentUserProfileComponent } from './components/user/current-user-profile/current-user-profile.component';
import { UpdateUserProfileComponent } from './components/user/update-user-profile/update-user-profile.component';
import { AdminRolesDashboardComponent } from './components/user/admin-roles-dashboard/admin-roles-dashboard.component';
import { AdminDataManagementDashboardComponent } from './components/user/admin-data-management-dashboard/admin-data-management-dashboard.component';

@NgModule({
  declarations: [
    AppComponent,
    BookComponent,
    LibraryComponent,
    ReaderComponent,
    NavbarComponent,
    FooterComponent,
    MainPageComponent,
    LibraryDetailsComponent,
    BookDetailsComponent,
    ReaderDetailsComponent,
    BookFilterComponent,
    LibraryBooksStatisticsComponent,
    LibraryReadersStatisticsComponent,
    LibraryAddComponent,
    LibraryUpdateComponent,
    LibraryDeleteComponent,
    BookUpdateComponent,
    BookDeleteComponent,
    ReaderUpdateComponent,
    ReaderDeleteComponent,
    ReaderAddComponent,
    BookAddComponent,
    ReaderNewMembershipComponent,
    LoginComponent,
    UserProfileComponent,
    RegisterComponent,
    RegisterConfirmationComponent,
    CurrentUserProfileComponent,
    UpdateUserProfileComponent,
    AdminRolesDashboardComponent,
    AdminDataManagementDashboardComponent
    // put all components, automat cand le creez
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ToastrModule.forRoot({
      positionClass: 'toast-bottom-center'
    }),
    BrowserAnimationsModule,
    MatInputModule,
    MatAutocompleteModule,
    MatPaginatorModule,
    // form module + http service module
  ],
  providers: [
    // all the services
    LibraryService,
    BookService,
    httpInterceptorProviders
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }

// ng g c Book - genereaza o componenta
