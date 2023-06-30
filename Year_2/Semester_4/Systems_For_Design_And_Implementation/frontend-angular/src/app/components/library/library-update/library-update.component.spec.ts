import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LibraryUpdateComponent } from './library-update.component';

describe('LibraryUpdateComponent', () => {
  let component: LibraryUpdateComponent;
  let fixture: ComponentFixture<LibraryUpdateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LibraryUpdateComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LibraryUpdateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
