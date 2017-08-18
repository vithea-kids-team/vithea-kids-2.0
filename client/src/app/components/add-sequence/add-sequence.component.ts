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
    public sequenceExercises: Array<Exercise> = [];
    public sequenceChildren: Array<Child> = [];
    loading = false;

    constructor(public route: ActivatedRoute, public sequencesService: SequencesService, public exercisesService: ExercisesService,
        public childrenService: ChildrenService, public router: Router) { }

    ngOnInit() {
        this.loading = true;
        this.route.params
            .switchMap((params: Params) => Observable.of(params))
            .subscribe(params => {
                this.loadExercisesToAdd();
                this.loadChildrenToAssign();
                const id: number = parseInt(params['childid'], 10);
                if (id) {
                    this.newSequence.childId = id;
                }
                const sequenceId: number = parseInt(params['sequenceid'], 10);
                if (sequenceId) {
                    this.sequencesService.getSequence(sequenceId).subscribe(
                        res => {
                            console.log(res);
                            this.newSequence.sequenceId = res.sequenceId;
                            this.newSequence.sequenceName = res.sequenceName;
                            this.sequenceExercises = res.sequenceExercises;
                            this.sequenceChildren = res.sequenceChildren;
                            //this.newSequence.orderExercises = res.sequenceOrderExercises;

                            //console.log(this.newSequence.orderExercises.length);

                            /*let newSequenceExercices: Array<Exercise> = [];
                            this.orderExercisesList.forEach(idExercise => {
                                this.sequenceExercises.forEach(exercise => {
                                    if (exercise.exerciseId === idExercise) {
                                        newSequenceExercices.push(exercise);
                                    }
                                })
                            })
                            this.sequenceExercises = newSequenceExercices;*/

                            this.sequenceExercises.forEach(exercise => {
                                this.addedExercises.push(exercise);
                                this.removeExerciseAdded(exercise);
                            })

                            this.sequenceChildren.forEach(child => {
                                this.addedChildren.push(child);
                                this.removeChildrenAdded(child);
                            })
                            this.loading = false;
                        },
                        err => {
                            console.log('Error getting sequence');
                            this.loading = false;
                        }
                      );
                } else {
                    this.newSequence.sequenceId = sequenceId;
                    this.loading = false;
                }
            });
    }
    registerSequence() {
        this.loading = true;

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
                this.loading = false;
            },
            err => {
                console.log('Error creating sequence.');
                    this.loading = false;
            })
    }

    editSequence() {
        this.loading = true;
        this.newSequence.exercisesToAdd = this.addedExercises.map((exercise) => {
            return exercise.exerciseId;
        });

        this.newSequence.childrenToAssign = this.addedChildren.map((child) => {
            return child.childId;
        });

        this.sequencesService.editSequence(this.newSequence)
            .subscribe(res => {
                if (this.newSequence.childId) {
                    this.router.navigate(['/children/' + this.newSequence.childId + '/sequences']);
                } else {
                    this.router.navigate(['/sequences']);
                }
                this.loading = false;
            },
            err => console.log('Error editing sequence.'));
            this.loading = false;
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

    /*orderExercises () {
        let newSequenceExercices: Array<Exercise> = [];
        this.sequenceOrderExercises.forEach(idExercise => {
            this.sequenceExercises.forEach(exercise => {
                if (exercise.exerciseId === idExercise) {
                    newSequenceExercices.push(exercise);
                }
            })
        })
        this.sequenceExercises = newSequenceExercices;
    }*/

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

    removeExerciseAdded(exercise: Exercise) {
        let i = 0;
        this.exercises.forEach(exercise2 => {
            if (exercise2.exerciseId === exercise.exerciseId) {
                let size = this.exercises.length;
                if (i === 0) {
                    this.exercises.reverse();
                    this.exercises.pop();
                } else if (i === size - 1) {
                    this.exercises.pop();
                } else {
                    let exercises1 = this.exercises.splice(0, i);
                    let exercises2 = this.exercises.splice(i, size);
                    this.exercises = exercises1.concat(exercises2);
                }
            } else {
                i++;
            }
        });
    }

    removeChildrenAdded(child: Child) {
        let i = 0;
        this.children.forEach(child2 => {
            if (child2.childId === child.childId) {
                let size = this.children.length;
                if (i === 0) {
                    this.children.reverse();
                    this.children.pop();
                } else if (i === size - 1) {
                    this.children.pop();
                } else {
                    let children1 = this.children.splice(0, i);
                    let children2 = this.children.splice(i, size);
                    this.children = children1.concat(children2);
                }
            } else {
                i++;
            }
        });
    }

    upExercise(index: number) {
        let size = this.addedExercises.length;
        if (index >= 1) {
            const first = 0;
            let last = size - 1;
            let previous = index - 1;
            let next = index + 1;
            if (size === 2) {
                this.addedExercises.reverse();
            } else if (size >= 3 ) {
                if (index === last) {
                    let tmpAddedExercises_1: Array<Exercise> = this.addedExercises.slice(first, previous);
                    let tmpAddedExercises_2: Array<Exercise> = this.addedExercises.slice(previous, size);
                    tmpAddedExercises_2.reverse();
                    this.addedExercises = tmpAddedExercises_1.concat(tmpAddedExercises_2);
                } else {
                    let tmpAddedExercises_1: Array<Exercise> = this.addedExercises.slice(first, previous);
                    let tmpAddedExercises_2: Array<Exercise> = this.addedExercises.slice(previous, next);
                    tmpAddedExercises_2.reverse();
                    let tmpAddedExercises_3: Array<Exercise> = this.addedExercises.slice(next, size);
                    this.addedExercises = tmpAddedExercises_1.concat(tmpAddedExercises_2, tmpAddedExercises_3);
                }
            }
        }
    }

    downExercise(index: number) {
        let size = this.addedExercises.length;
        if (index < size - 1) {
            const first = 0;
            let last = size - 1;
            let next = index + 1;
            let next2 = index + 2;

            if (size === 2) {
                this.addedExercises.reverse();
            } else if (size >= 3) {
                if (index === first) {
                    let tmpAddedExercises_1: Array<Exercise> = this.addedExercises.slice(first, next2);
                    tmpAddedExercises_1.reverse();
                    let tmpAddedExercises_2: Array<Exercise> = this.addedExercises.slice(next2, size);
                    this.addedExercises = tmpAddedExercises_1.concat(tmpAddedExercises_2);
                } else {
                    let tmpAddedExercises_1: Array<Exercise> = this.addedExercises.slice(first, index);
                    let tmpAddedExercises_2: Array<Exercise> = this.addedExercises.slice(index, next2);
                    tmpAddedExercises_2.reverse();
                    let tmpAddedExercises_3: Array<Exercise> = this.addedExercises.slice(next2, size);
                    this.addedExercises = tmpAddedExercises_1.concat(tmpAddedExercises_2, tmpAddedExercises_3);
                }
            }
        }
    }
}
