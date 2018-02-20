/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { EditExerciseMultipleChoiceTextComponent } from './edit-exercise-mc-text.component';

describe('EditExerciseMultipleChoiceTextComponent', () => {
  let component: EditExerciseMultipleChoiceTextComponent;
  let fixture: ComponentFixture<EditExerciseMultipleChoiceTextComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EditExerciseMultipleChoiceTextComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EditExerciseMultipleChoiceTextComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
