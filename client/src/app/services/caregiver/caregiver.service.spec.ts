/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { CaregiverService } from './caregiver.service';

describe('CaregiverService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [CaregiverService]
    });
  });

  it('should ...', inject([CaregiverService], (service: CaregiverService) => {
    expect(service).toBeTruthy();
  }));
});
