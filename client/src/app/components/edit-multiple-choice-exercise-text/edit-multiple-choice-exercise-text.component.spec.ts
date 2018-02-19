/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { EditMultipleChoiceExerciseTextComponent } from './edit-multiple-choice-exercise-text.component';

describe('EditMultipleChoiceExerciseTextComponent', () => {
  let component: EditMultipleChoiceExerciseTextComponent;
  let fixture: ComponentFixture<EditMultipleChoiceExerciseTextComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EditMultipleChoiceExerciseTextComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EditMultipleChoiceExerciseTextComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
