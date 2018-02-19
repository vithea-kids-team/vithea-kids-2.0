/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { AddMultipleChoiceExerciseTextComponent } from './add-multiple-choice-exercise-text.component';

describe('AddMultipleChoiceExerciseTextComponent', () => {
  let component: AddMultipleChoiceExerciseTextComponent;
  let fixture: ComponentFixture<AddMultipleChoiceExerciseTextComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddMultipleChoiceExerciseTextComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddMultipleChoiceExerciseTextComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
