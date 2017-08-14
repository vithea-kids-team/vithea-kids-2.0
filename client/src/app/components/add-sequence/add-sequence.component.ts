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
        const sequenceId: number = parseInt(params['sequenceid'], 10);
        console.log('sequenceid ' + sequenceId);
        if (sequenceId) {
          this.newSequence.sequenceId = sequenceId;
        } else {
          //TODO:
        }

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
        let i = 0;
        if (this.newSequence.childId) {
          this.children.forEach(child => {
            if (child.childId === this.newSequence.childId) {
              this.addChild(i);
            } else {
              i++;
            }
          });
        }
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

  upExercise(index: number) {
    let size = this.addedExercises.length;

    if (index >= 1) {
        console.log('index: ' + index + ' size: ' + size);

        if (size === 2) {
            this.addedExercises.reverse();
        } else if (size === 3) {

        } else if (size >= 4 ) {

        }
            // see if the element to change is the last one
            //

            /* let index2 = index--;
            let index3 = index++;
            let last = size - 1;

            console.log('0,' + index2 + ',' + index + ',' + index3 + ',' + last);

            let tmpAddedExercises_1: Array<Exercise> = this.addedExercises.slice(0, index2);        // until index-1 to be swapped
            let tmpAddedExercises_2: Array<Exercise> = this.addedExercises.slice(index2, index);    // first element to be swapped
            let tmpAddedExercises_3: Array<Exercise> = this.addedExercises.slice(index, index3);    // second element to be swapped
            let tmpAddedExercises_4: Array<Exercise> = this.addedExercises.slice(index3, last);     // rest

            let tmpAddedExercises: Array<Exercise> = tmpAddedExercises_1.concat(tmpAddedExercises_3, tmpAddedExercises_2, tmpAddedExercises_4);

            this.addedExercises = tmpAddedExercises;*/
    }

    /*this.addedExercises.forEach(exercise => {
        if (iterator !== index2) {
            tmpAddedExercises.push(exercise);
        }
        else {
        }
        iterator++;
    });


    let iterator = 0;
    let exercise: Array<Exercise> = [];
    if (index >= 1) {
        exercise = this.addedExercises.splice(index, 1); // remove the element
        this.addedExercises.splice(index - 1, exercise.pop() );
*/
  }

  downExercise(index: number) {

  }

  isValidFormUp(index: number) {
    if (index === 0) {
      return false;
    } else {
      return true;
    }
  }

  isValidFormDown(index: number) {
    if (index === this.addedExercises.length - 1) {
      return false;
    } else {
      return true;
    }
  }
}
