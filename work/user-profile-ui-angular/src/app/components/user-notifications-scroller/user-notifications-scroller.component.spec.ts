import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserNotificationsScrollerComponent } from './user-notifications-scroller.component';

describe('UserNotificationsScrollerComponent', () => {
  let component: UserNotificationsScrollerComponent;
  let fixture: ComponentFixture<UserNotificationsScrollerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UserNotificationsScrollerComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UserNotificationsScrollerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
