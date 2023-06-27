import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FinalrouteComponent } from './finalroute.component';

describe('FinalrouteComponent', () => {
  let component: FinalrouteComponent;
  let fixture: ComponentFixture<FinalrouteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FinalrouteComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FinalrouteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
