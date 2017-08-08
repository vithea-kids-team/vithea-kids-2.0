import { Injectable } from '@angular/core';
import { HttpApiClient } from '../http/http-api-client.service';

@Injectable()
export class SequencesService {

  constructor(public http: HttpApiClient) { }

  getSequences() {
    return this.http.get('/listsequences').map(result => result && result.json());
  }

  registerSequence(sequence) {
    return this.http.post('/registersequence', sequence).map(result => result && result.json());
  }

  editSequence(sequence) {
    return this.http.post('/editsequence/' + sequence.sequenceId, sequence);
  }

  getSequenceChildren(sequence) {
    return this.http.get('/sequences/' + sequence.sequenceId).map(result => result && result.json());
  }

  deleteSequence(sequenceId) {
    return this.http.delete('/deletesequence/' + sequenceId);
  }
}
