import { Injectable } from '@angular/core';
import { Http } from '@angular/http'
import { Configuration } from '../config/app.config';

@Injectable()
export class ExercisesService {

  constructor (private http : Http, private config : Configuration) {}

  getExercises() {
    return this.http.get(this.config.ServerWithApiUrl + 'listexercises')
            .map(result => result.json());
  }
}