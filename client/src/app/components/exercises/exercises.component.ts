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

  private exercises: Array<Exercise>;
  private sequenceId: number = 0;
  private sequence;
  private loading : boolean = false;

  private textFilter = true;
  private imageFilter = true;
  
  constructor(private route: ActivatedRoute, private exercisesService : ExercisesService, private childrenService : ChildrenService) { }

  ngOnInit() {  
    this.fetchExercises();
  }

  private fetchExercises() {
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

  private getSequenceExercises(id) {
    this.childrenService.getSequence(id).subscribe(
      res => {
        this.sequence = res.json();
        this.exercises = res.json().sequenceExercises;
        this.loading = false;
      },
      err => console.log("Error loading sequence exercises", err)
    )
  }

  private getExercises() {
    this.exercisesService.getExercises().subscribe(
      result => {
        this.exercises = result;
        this.loading = false;
      },
      err => console.error("Error loading exercises. ", err) 
    );
  }

  private deleteExercise(exerciseId) {
    this.loading = true;
    this.exercisesService.deleteExercise(exerciseId).subscribe(
      res => this.fetchExercises(),
      err => console.log("Error deleting exercise", exerciseId)
    )
  }
}
