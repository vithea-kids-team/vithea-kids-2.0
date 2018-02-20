/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { AddExerciseMultipleChoiceTextComponent } from './add-exercise-mc-text.component';

describe('AddExerciseMultipleChoiceTextComponent', () => {
  let component: AddExerciseMultipleChoiceTextComponent;
  let fixture: ComponentFixture<AddExerciseMultipleChoiceTextComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddExerciseMultipleChoiceTextComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddExerciseMultipleChoiceTextComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
