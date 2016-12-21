import { Injectable } from '@angular/core';
import { HttpApiClient } from '../http/http-api-client.service';

@Injectable()
export class ExercisesService {

  constructor (private http : HttpApiClient) {}

  getExercises() {
    return this.http.get('/app/listexercises')
            .map(result => result.json());
  }

}
