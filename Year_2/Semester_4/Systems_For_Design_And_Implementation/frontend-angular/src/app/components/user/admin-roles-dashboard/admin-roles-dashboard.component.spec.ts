import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminRolesDashboardComponent } from './admin-roles-dashboard.component';

describe('AdminRolesDashboardComponent', () => {
  let component: AdminRolesDashboardComponent;
  let fixture: ComponentFixture<AdminRolesDashboardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdminRolesDashboardComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminRolesDashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
