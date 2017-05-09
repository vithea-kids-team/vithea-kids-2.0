/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { HttpApiClient } from './http-api-client.service';

describe('HttpApiClient', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [HttpApiClient]
    });
  });

  it('should ...', inject([HttpApiClient], (service: HttpApiClient) => {
    expect(service).toBeTruthy();
  }));
});
