import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params }   from '@angular/Router';
import { Observable } from 'rxjs/Observable';
import { Router } from '@angular/Router';

import { Sequence } from '../../models/sequence';
import { Exercise } from '../../models/exercise';
import { Child } from '../../models/child';

import { SequencesService } from '../../services/sequences/sequences.service';
import { ExercisesService } from '../../services/exercises/exercises.service';
import { ChildrenService } from '../../services/children/children.service';

@Component({
  selector: 'app-add-sequence',
  templateUrl: './add-sequence.component.html',
  styleUrls: ['./add-sequence.component.css']
})
export class AddSequenceComponent implements OnInit {

  public newSequence = new Sequence();
  public exercises: Array<Exercise>;
  public children: Array<Child>;
  public addedExercises: Array<Exercise> = [];
  public addedChildren: Array<Child> = [];

  constructor(public route: ActivatedRoute, public sequencesService: SequencesService, public exercisesService: ExercisesService,
    public childrenService: ChildrenService, public router: Router) { }

  ngOnInit() {
     this.route.params
      .switchMap((params: Params) => Observable.of(params))
      .subscribe(params => {
        const id: number = parseInt(params['childid'], 10);
        if (id) {
          this.newSequence.childId = id;
        }
      });

      this.loadExercisesToAdd();
      this.loadChildrenToAssign();
  }

  registerSequence() {
    this.newSequence.exercisesToAdd = this.addedExercises.map((exercise) => {
      return exercise.exerciseId;
    });

    this.newSequence.childrenToAssign = this.addedChildren.map((child) => {
      return child.childId;
    });

    this.sequencesService.registerSequence(this.newSequence)
      .subscribe(res => {
        if (this.newSequence.childId) {
          this.router.navigate(['/children/' + this.newSequence.childId + '/sequences']);
        } else {
          this.router.navigate(['/sequences']);
        }
      },
      err => console.log('Error creating sequence.'));
  }

  loadExercisesToAdd() {
     this.exercisesService.getExercises().subscribe(
      result => {
        this.exercises = result;
      },
      err => console.error('Error loading exercises for adding to sequence.', err)
    );
  }

  loadChildrenToAssign() {
    this.childrenService.getChildren().subscribe(
      result => {
        this.children = result;
      },
      err => console.error('Error loading children for assigning to sequence.', err)
    );
  }

  removeExercise(index: number) {
    this.exercises.push(this.addedExercises[index]);
    this.addedExercises.splice(index, 1);
  }

  addExercise(index: number) {
    this.addedExercises.push(this.exercises[index]);
    this.exercises.splice(index, 1);
  }

  removeChild(index: number) {
    this.children.push(this.addedChildren[index]);
    this.addedChildren.splice(index, 1);
  }

  addChild(index: number) {
    this.addedChildren.push(this.children[index]);
    this.children.splice(index, 1);
  }
}
