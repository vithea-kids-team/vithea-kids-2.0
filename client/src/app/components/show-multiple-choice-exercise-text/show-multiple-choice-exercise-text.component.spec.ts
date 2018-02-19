/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { ShowMultipleChoiceExerciseTextComponent } from './show-multiple-choice-exercise-text.component';

describe('ShowMultipleChoiceExerciseTextComponent', () => {
  let component: ShowMultipleChoiceExerciseTextComponent;
  let fixture: ComponentFixture<ShowMultipleChoiceExerciseTextComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ShowMultipleChoiceExerciseTextComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ShowMultipleChoiceExerciseTextComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
