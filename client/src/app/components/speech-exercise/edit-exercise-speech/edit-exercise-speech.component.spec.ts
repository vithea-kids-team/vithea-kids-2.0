/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { EditExerciseSpeechComponent } from './edit-exercise-speech.component';

describe('EditExerciseSpeechComponent', () => {
  let component: EditExerciseSpeechComponent;
  let fixture: ComponentFixture<EditExerciseSpeechComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EditExerciseSpeechComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EditExerciseSpeechComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
