/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { ShowExerciseMultipleChoiceTextComponent } from './show-exercise-mc-text.component';

describe('ShowExerciseMultipleChoiceTextComponent', () => {
  let component: ShowExerciseMultipleChoiceTextComponent;
  let fixture: ComponentFixture<ShowExerciseMultipleChoiceTextComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ShowExerciseMultipleChoiceTextComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ShowExerciseMultipleChoiceTextComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
