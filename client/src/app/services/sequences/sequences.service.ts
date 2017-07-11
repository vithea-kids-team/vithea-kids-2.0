import { Injectable } from '@angular/core';
import { HttpApiClient } from '../http/http-api-client.service';

@Injectable()
export class SequencesService {

  constructor(public http : HttpApiClient) { }

  getSequences() {
    return this.http.get('/listsequences')
            .map(result => result && result.json());
  }

  registerSequence(sequence) {
    return this.http.post('/registersequence', sequence)
            .map(result => result && result.json());
  }

  deleteSequence(sequenceId) {
    return this.http.delete('/deletesequence/' + sequenceId);
  }

}
