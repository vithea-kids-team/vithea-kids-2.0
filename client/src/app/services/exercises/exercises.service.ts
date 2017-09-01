import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { Response } from '@angular/http';
import { HttpApiClient } from '../http/http-api-client.service';
import { Exercise } from '../../models/exercise'


@Injectable()
export class ExercisesService {

  exercises: Array<any>
  success = false
  failure = false;
  textSuccess;
  textFailure;

  // temporary info for going to preferences, and when return have the form filled with the previous information
  edited;
  exerciseId;
  exerciseName;
  exerciseType;
  exerciseTopic;
  exerciseLevel;
  exerciseQuestion;
  exerciseRightAnswer;
  exerciseStimulusText;
  exerciseStimulus;
  exerciseDistractor1;
  exerciseDistractor2;
  exerciseDistractor3;

  constructor (public http: HttpApiClient) {}

  getExercises(): Observable<Array<Exercise>> {
    return this.http.get('/listexercises').map(result => this.exercises = result && result.json());
  }

  getExercise(id: number): Observable<Exercise> {
    return this.http.get('/exercise/' + id).map(result => result && result.json());
  }

  addExercise(exercise: Exercise): Observable<Response> {
    return this.http.post('/registerexercise', exercise);
  }

  editExercise(exercise: Exercise): Observable<Response> {
    return this.http.post('/editexercise/' + exercise.exerciseId, exercise);
  }

  deleteExercise(id: number) {
    return this.http.delete('/deleteexercise/' + id);
  }

  getSuccess() {
    return this.success;
  }
  getFailure() {
    return this.failure;
  }
  getTextSuccess() {
    return this.textSuccess;
  }
  getTextFailure() {
    return this.textFailure;
  }
  setSuccess(success: boolean) {
    this.success = success;
  }
  setFailure(failure: boolean) {
    this.failure = failure;
  }
  setTextSuccess(text: string) {
    this.textSuccess = text;
  }
  setTextFailure(text: string) {
    this.textFailure = text;
  }

}
