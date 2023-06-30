import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReaderNewMembershipComponent } from './reader-new-membership.component';

describe('ReaderNewMembershipComponent', () => {
  let component: ReaderNewMembershipComponent;
  let fixture: ComponentFixture<ReaderNewMembershipComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ReaderNewMembershipComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReaderNewMembershipComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
