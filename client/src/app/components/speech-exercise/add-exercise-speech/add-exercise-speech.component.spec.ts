/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { AddExerciseSpeechComponent } from './add-exercise-speech.component';

describe('AddExerciseSpeechComponent', () => {
  let component: AddExerciseSpeechComponent;
  let fixture: ComponentFixture<AddExerciseSpeechComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddExerciseSpeechComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddExerciseSpeechComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
