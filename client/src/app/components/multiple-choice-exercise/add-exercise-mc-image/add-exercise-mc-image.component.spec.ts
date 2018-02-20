/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { AddExerciseMultipleChoiceImageComponent } from './add-exercise-mc-image.component';

describe('AddExerciseMultipleChoiceImageComponent', () => {
  let component: AddExerciseMultipleChoiceImageComponent;
  let fixture: ComponentFixture<AddExerciseMultipleChoiceImageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddExerciseMultipleChoiceImageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddExerciseMultipleChoiceImageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
