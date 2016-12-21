/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { ChildrenService } from './children.service';

describe('ChildrenService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ChildrenService]
    });
  });

  it('should ...', inject([ChildrenService], (service: ChildrenService) => {
    expect(service).toBeTruthy();
  }));
});
