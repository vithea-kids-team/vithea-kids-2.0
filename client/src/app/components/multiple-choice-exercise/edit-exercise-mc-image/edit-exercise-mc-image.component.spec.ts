/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { EditExerciseMultipleChoiceImageComponent } from './edit-exercise-mc-image.component';

describe('EditExerciseMultipleChoiceImageComponent', () => {
  let component: EditExerciseMultipleChoiceImageComponent;
  let fixture: ComponentFixture<EditExerciseMultipleChoiceImageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EditExerciseMultipleChoiceImageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EditExerciseMultipleChoiceImageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
