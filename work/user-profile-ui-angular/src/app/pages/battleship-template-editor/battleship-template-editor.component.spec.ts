import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BattleshipTemplateEditorComponent } from './battleship-template-editor.component';

describe('BattleshipTemplateEditorComponent', () => {
  let component: BattleshipTemplateEditorComponent;
  let fixture: ComponentFixture<BattleshipTemplateEditorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BattleshipTemplateEditorComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BattleshipTemplateEditorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
