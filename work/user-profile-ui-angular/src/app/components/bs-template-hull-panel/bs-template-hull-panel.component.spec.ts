import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BsTemplateHullPanelComponent } from './bs-template-hull-panel.component';

describe('BsTemplateHullPanelComponent', () => {
  let component: BsTemplateHullPanelComponent;
  let fixture: ComponentFixture<BsTemplateHullPanelComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BsTemplateHullPanelComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BsTemplateHullPanelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
