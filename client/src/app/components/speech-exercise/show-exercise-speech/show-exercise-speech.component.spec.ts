/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { ShowExerciseSpeechComponent } from './show-exercise-speech.component';

describe('ShowExerciseSpeechComponent', () => {
  let component: ShowExerciseSpeechComponent;
  let fixture: ComponentFixture<ShowExerciseSpeechComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ShowExerciseSpeechComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ShowExerciseSpeechComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
