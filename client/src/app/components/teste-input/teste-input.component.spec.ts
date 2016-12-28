/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { TesteInputComponent } from './teste-input.component';

describe('TesteInputComponent', () => {
  let component: TesteInputComponent;
  let fixture: ComponentFixture<TesteInputComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TesteInputComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TesteInputComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
