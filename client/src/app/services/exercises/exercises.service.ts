import { Injectable } from '@angular/core';
import { HttpApiClient } from '../http/http-api-client.service';
import { Observable } from 'rxjs/Observable';
import { Response } from '@angular/http';
import { Exercise } from '../../models/exercise'


@Injectable()
export class ExercisesService {

  constructor (private http : HttpApiClient) {}

  getExercises() {
    return this.http.get('/listexercises')
            .map(result => result && result.json());
  }

  registerExercise(exercise) {
    return this.http.post('/registerexercise', exercise)
            .map(result => result && result.json());
  }

  deleteExercise(exerciseId) {
    return this.http.delete('/deleteexercise/'+ exerciseId);
  }

  getExercise(id : number): Observable<Exercise> {
    return this.http.get('/exercise/' + id)
      .map(result => result && result.json());
  }

}
