import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';
import { ShowExerciseMultipleChoiceImageComponent } from './show-exercise-mc-image.component';

describe('ShowExerciseMultipleChoiceImageComponent', () => {
  let component: ShowExerciseMultipleChoiceImageComponent;
  let fixture: ComponentFixture<ShowExerciseMultipleChoiceImageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ShowExerciseMultipleChoiceImageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ShowExerciseMultipleChoiceImageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
