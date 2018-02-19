/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';
import { ShowMultipleChoiceExerciseImageComponent } from './show-multiple-choice-exercise-image.component';

describe('ShowMultipleChoiceExerciseImageComponent', () => {
  let component: ShowMultipleChoiceExerciseImageComponent;
  let fixture: ComponentFixture<ShowMultipleChoiceExerciseImageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ShowMultipleChoiceExerciseImageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ShowMultipleChoiceExerciseImageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
