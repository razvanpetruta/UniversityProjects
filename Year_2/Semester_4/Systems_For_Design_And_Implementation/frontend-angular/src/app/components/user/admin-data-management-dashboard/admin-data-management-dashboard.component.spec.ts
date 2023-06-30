import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminDataManagementDashboardComponent } from './admin-data-management-dashboard.component';

describe('AdminDataManagementDashboardComponent', () => {
  let component: AdminDataManagementDashboardComponent;
  let fixture: ComponentFixture<AdminDataManagementDashboardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdminDataManagementDashboardComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminDataManagementDashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
