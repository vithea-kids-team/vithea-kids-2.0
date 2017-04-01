import 'rxjs/add/operator/switchMap';

import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params }   from '@angular/Router';
import { Observable } from 'rxjs/Observable';

import { Exercise } from '../../models/Exercise';
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
  
  constructor(private route: ActivatedRoute, private exercisesService : ExercisesService, private childrenService : ChildrenService) { }

  ngOnInit() {  
    this.route.params
      .switchMap((params: Params) => Observable.of(params))
      .subscribe(params => {
        const id : number = parseInt(params['id']);
        if(id) {
          this.sequenceId = id;
          this.exercises = this.childrenService.getSequence(id).sequenceExercises;
        } else {
          this.getExercises();
        }
      });
  }

  private getExercises() {
    this.exercisesService.getExercises().subscribe(
      result => this.exercises = result, 
      err => console.error("Error loading exercises. " + err) 
    );
  }
}
