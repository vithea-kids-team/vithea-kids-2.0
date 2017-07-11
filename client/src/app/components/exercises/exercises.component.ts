import 'rxjs/add/operator/switchMap';

import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params }   from '@angular/Router';
import { Observable } from 'rxjs/Observable';

import { Exercise } from '../../models/exercise';
import { ExercisesService } from '../../services/exercises/exercises.service';
import { ChildrenService } from '../../services/children/children.service';

@Component({
  selector: 'app-exercises',
  templateUrl: './exercises.component.html',
  styleUrls: ['./exercises.component.css']
})
export class ExercisesComponent implements OnInit {

  public exercises: Array<Exercise>;
  public sequenceId: number = 0;
  public sequence;
  public loading : boolean = false;

  public textFilter = true;
  public imageFilter = true;
  
  constructor(public route: ActivatedRoute, public exercisesService : ExercisesService, public childrenService : ChildrenService) { }

  ngOnInit() {  
    this.fetchExercises();
  }

  public fetchExercises() {
    this.loading = true;
    this.route.params
      .switchMap((params: Params) => Observable.of(params))
      .subscribe(params => {
        const id : number = parseInt(params['sequenceid']);
        if(id) {
          this.sequenceId = id;
          this.getSequenceExercises(id);
        } else {
          this.getExercises();
        }
      },
      err => {
        console.error("Error loading exercises", err);
        this.loading = false;
      });
  }

  public getSequenceExercises(id) {
    this.childrenService.getSequence(id).subscribe(
      res => {
        this.sequence = res.json();
        this.exercises = res.json().sequenceExercises;
        this.loading = false;
      },
      err => console.log("Error loading sequence exercises", err)
    )
  }

  public getExercises() {
    this.exercisesService.getExercises().subscribe(
      result => {
        this.exercises = result;
        this.loading = false;
      },
      err => console.error("Error loading exercises. ", err) 
    );
  }

  public deleteExercise(exerciseId) {
    this.loading = true;
    this.exercisesService.deleteExercise(exerciseId).subscribe(
      res => this.fetchExercises(),
      err => console.log("Error deleting exercise", exerciseId)
    )
  }
}
