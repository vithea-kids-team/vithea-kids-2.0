/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { AddExerciseSelectionImageComponent } from './add-exercise-selectionImage.component';

describe('AddExerciseComponent', () => {
  let component: AddExerciseSelectionImageComponent;
  let fixture: ComponentFixture<AddExerciseSelectionImageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddExerciseSelectionImageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddExerciseSelectionImageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
