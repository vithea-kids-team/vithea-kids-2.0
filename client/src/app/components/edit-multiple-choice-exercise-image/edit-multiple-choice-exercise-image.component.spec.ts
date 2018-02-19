/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { EditMultipleChoiceExerciseImageComponent } from './edit-multiple-choice-exercise-image.component';

describe('EditMultipleChoiceExerciseImageComponent', () => {
  let component: EditMultipleChoiceExerciseImageComponent;
  let fixture: ComponentFixture<EditMultipleChoiceExerciseImageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EditMultipleChoiceExerciseImageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EditMultipleChoiceExerciseImageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
