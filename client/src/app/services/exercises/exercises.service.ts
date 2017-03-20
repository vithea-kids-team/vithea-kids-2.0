import { Injectable } from '@angular/core';
import { HttpApiClient } from '../http/http-api-client.service';

@Injectable()
export class ExercisesService {

  constructor (private http : HttpApiClient) {}

  getExercises() {
    return this.http.get('/listexercises')
            .map(result => result.json());
  }

  registerExercise(exercise) {
    return this.http.post('/registerexercise', exercise)
            .map(result => result.json());
  }

}
