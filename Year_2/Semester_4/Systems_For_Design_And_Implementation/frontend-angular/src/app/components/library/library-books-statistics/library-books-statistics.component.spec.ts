import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LibraryBooksStatisticsComponent } from './library-books-statistics.component';

describe('LibraryBooksStatisticsComponent', () => {
  let component: LibraryBooksStatisticsComponent;
  let fixture: ComponentFixture<LibraryBooksStatisticsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LibraryBooksStatisticsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LibraryBooksStatisticsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
