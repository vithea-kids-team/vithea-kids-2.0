/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { HttpApiClientService } from './http-api-client.service';

describe('HttpApiClientService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [HttpApiClientService]
    });
  });

  it('should ...', inject([HttpApiClientService], (service: HttpApiClientService) => {
    expect(service).toBeTruthy();
  }));
});
