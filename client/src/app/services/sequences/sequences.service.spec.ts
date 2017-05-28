/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { SequencesService } from './sequences.service';

describe('SequencesService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [SequencesService]
    });
  });

  it('should ...', inject([SequencesService], (service: SequencesService) => {
    expect(service).toBeTruthy();
  }));
});
