import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StatisticsSequenceComponent } from './statistics-sequence.component';

describe('StatisticsSequenceComponent', () => {
  let component: StatisticsSequenceComponent;
  let fixture: ComponentFixture<StatisticsSequenceComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ StatisticsSequenceComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StatisticsSequenceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
