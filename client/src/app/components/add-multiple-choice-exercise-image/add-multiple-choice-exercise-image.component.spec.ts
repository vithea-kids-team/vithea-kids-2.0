/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { AddMultipleChoiceExerciseImageComponent } from './add-multiple-choice-exercise-image.component';

describe('AddMultipleChoiceExerciseImageComponent', () => {
  let component: AddMultipleChoiceExerciseImageComponent;
  let fixture: ComponentFixture<AddMultipleChoiceExerciseImageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddMultipleChoiceExerciseImageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddMultipleChoiceExerciseImageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
