import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LibraryReadersStatisticsComponent } from './library-readers-statistics.component';

describe('LibraryReadersStatisticsComponent', () => {
  let component: LibraryReadersStatisticsComponent;
  let fixture: ComponentFixture<LibraryReadersStatisticsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LibraryReadersStatisticsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LibraryReadersStatisticsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
