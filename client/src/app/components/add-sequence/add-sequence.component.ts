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
import { Overlay } from 'ngx-modialog';
import { Modal } from 'ngx-modialog/plugins/bootstrap';
import { Location } from '@angular/common';

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

    public nameError;
    public exercisesAddedError;
    public childrenAddedError;

    constructor(public route: ActivatedRoute, public sequencesService: SequencesService, public exercisesService: ExercisesService,
        public childrenService: ChildrenService, public router: Router, public modal: Modal, public location: Location) { }

    validateNameSequence() {
        if (this.newSequence.sequenceName === undefined) {
            this.nameError = true;
          } else {
            this.nameError = false;
            let sequenceName = this.newSequence.sequenceName.replace(/\s+/g, '');
            if (sequenceName.length === 0) {
              this.nameError = true;
            } else {
              this.nameError = false;
            }
          }
    }

    validateExercisesAdded() {
        if (this.addedExercises.length >= 1) {
            this.exercisesAddedError = false;
        } else {
            this.exercisesAddedError = true;
        }
    }

    validateChildrenAdded() {
        if (this.addedChildren.length >= 1) {
            this.childrenAddedError = false;
        } else {
            this.childrenAddedError = true;
        }
    }

    submitRegisterSequence() {

        this.validateChildrenAdded();
        this.validateExercisesAdded();
        this.validateNameSequence();

        if (this.childrenAddedError === false && this.exercisesAddedError === false && this.nameError === false) {
            this.registerSequence();
        }
    }

    submitEditSequence() {
        this.validateChildrenAdded();
        this.validateExercisesAdded();
        this.validateNameSequence();

        if (this.childrenAddedError === false && this.exercisesAddedError === false && this.nameError === false) {
            this.editSequence();
        }
    }

    ngOnInit() {
        this.loading = true;
        this.route.params
            .switchMap((params: Params) => Observable.of(params))
            .subscribe(params => {

                const id: number = parseInt(params['childid'], 10);
                if (id) {
                    this.newSequence.childId = id;
                }

                const sequenceId: number = parseInt(params['sequenceid'], 10);
                if (sequenceId) {
                    this.sequencesService.getSequence(sequenceId).subscribe(
                        res => {
                            this.newSequence.sequenceId = res.sequenceId;
                            this.newSequence.sequenceName = res.sequenceName;

                            // not the list of exercises, but only the ids and orders
                            let sequenceExercisesOrder = res.sequenceExercisesList;
                            this.sequenceChildren = res.sequenceChildren;
                            let newSequenceExercices: Array<Exercise> = [];
                            let newSequenceOrder: Array<number> = [];
                            let addedExercises2: Array<Exercise> = [];
                            let exercise;

                            this.exercisesService.getExercises().subscribe(
                                result => {
                                    this.exercises = result;
                                    sequenceExercisesOrder.forEach(exor => {
                                        exercise = this.findExerciseById(exor.sequenceExerciseId.exercise_id);
                                        if (exercise !== null) {
                                            newSequenceOrder.push(exor.exerciseOrder);
                                            addedExercises2.push(exercise);
                                            this.removeExerciseAdded(exercise);
                                        }
                                    });

                                    // rearrange the list of exercises to show them according to the orders' list
                                    let exercicesWithOrder = [];

                                    while (newSequenceOrder.length !== 0 && addedExercises2.length !== 0) {
                                        let exOr = [newSequenceOrder.pop(), addedExercises2.pop()];
                                        exercicesWithOrder.push(exOr);
                                    }

                                    let i = 1;
                                    while (this.addedExercises.length < exercicesWithOrder.length) {
                                        exercicesWithOrder.forEach(exOr => {
                                            if (exOr[0] === i) {
                                                this.addedExercises.push(exOr[1]);
                                            }
                                        });
                                        i++;
                                    }
                                },
                                err => {
                                    console.error('Error loading exercises for adding to sequence.', err);
                                }
                            );

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
                                    this.sequenceChildren.forEach(child => {
                                        this.addedChildren.push(child);
                                        this.removeChildrenAdded(child);
                                    })
                                },
                                err => {
                                    console.error('Error loading children for assigning to sequence.', err);
                                }
                            );
                            this.loading = false;
                        },
                        err => {
                            console.log('Error getting sequence');
                            this.loading = false;
                        }
                      );
                } else {
                    this.newSequence.sequenceId = sequenceId;

                    this.exercisesService.getExercises().subscribe(
                        result => {
                            this.exercises = result;
                        },
                        err => {
                            console.error('Error loading exercises for adding to sequence.', err);
                        }
                    );
                    this.childrenService.getChildren().subscribe(
                        result => {
                            this.children = result;
                        },
                        err => {
                            console.error('Error loading children for adding to sequence.', err);
                        }
                    );
                    this.loading = false;
                }
            });
    }

    findExerciseById(exerciseId: number) {
        console.log(exerciseId);
        console.log(this.exercises);
        let length = this.exercises.length;
        for (let i = 0; i < length; i++) {
            let exercise = this.exercises[i];
            if (exercise.exerciseId === exerciseId) {
                return exercise;
            }
        }
        return null;
    }

    registerSequence() {

        let sequenceName = this.newSequence.sequenceName;

        const dialogRef = this.modal.confirm().size('lg').isBlocking(true).showClose(false).okBtn('Sim').cancelBtn('Não')
        .title('Registar aula').body('Tem a certeza que pretende registar a aula ' + sequenceName + '?').open();

        dialogRef.then(dialogRef => { dialogRef.result.then(result => {
            if (result) {
                this.newSequence.exercisesToAdd = this.addedExercises.map((exercise) => {
                    return exercise.exerciseId;
                });

                this.newSequence.childrenToAssign = this.addedChildren.map((child) => {
                    return child.childId;
                });

                this.sequencesService.registerSequence(this.newSequence).subscribe(
                res => {
                    if (this.newSequence.childId) {
                        this.router.navigate(['/children/' + this.newSequence.childId + '/sequences']);
                    } else {
                        this.router.navigate(['/sequences']);
                    }
                    this.sequencesService.setSuccess(true);
                    this.sequencesService.setFailure(false);
                    this.sequencesService.setTextSuccess('Aula ' + sequenceName + ' registada com sucesso.');
                },
                err => {
                  console.error('Error registering new sequence.', err);
                  this.sequencesService.setSuccess(false);
                  this.sequencesService.setFailure(true);
                  this.sequencesService.setTextFailure('Não foi possível registar a aula ' + sequenceName + '.');
                }
              );
            } else {
              this.goBack();
            }
        }).catch(() => {})});
    }

    editSequence() {
        let sequenceName = this.newSequence.sequenceName;

        const dialogRef = this.modal.confirm().size('lg').isBlocking(true).showClose(false).okBtn('Sim').cancelBtn('Não')
        .title('Registar aula').body('Tem a certeza que pretende editar a aula ' + sequenceName + '?').open();

        dialogRef.then(dialogRef => { dialogRef.result.then(result => {
            if (result) {
                this.newSequence.exercisesToAdd = this.addedExercises.map((exercise) => {
                    return exercise.exerciseId;
                });

                this.newSequence.childrenToAssign = this.addedChildren.map((child) => {
                    return child.childId;
                });

                this.sequencesService.editSequence(this.newSequence).subscribe(
                    res => {
                        this.sequencesService.setSuccess(true);
                        this.sequencesService.setFailure(false);
                        this.sequencesService.setTextSuccess('Aula ' + sequenceName + ' editada com sucesso.');
                        if (this.newSequence.childId) {
                            this.router.navigate(['/children/' + this.newSequence.childId + '/sequences']);
                        } else {
                            this.router.navigate(['/sequences']);
                        }
                    },
                    err => {
                        console.error('Error editing new sequence.', err);
                        this.sequencesService.setSuccess(false);
                        this.sequencesService.setFailure(true);
                        this.sequencesService.setTextFailure('Não foi possível editar a aula ' + sequenceName + '.');
                    }
                );
            } else {
                this.goBack();
            }
        }).catch(() => {})});
    }

    public truncate(str: String, size: number) {
        if (str != null) {
          let result: String = str;
          if (str.length > size) {
            result = str.substring(0, size + 1) + ' (...)';
          }
          return result;
        } else {
          return '';
        }
      }

    removeExercise(index: number) {
        this.exercises.push(this.addedExercises[index]);
        this.addedExercises.splice(index, 1);
        this.validateExercisesAdded();
    }

    addExercise(index: number) {
        this.addedExercises.push(this.exercises[index]);
        this.exercises.splice(index, 1);
        this.validateExercisesAdded();
    }

    removeChild(index: number) {
        this.children.push(this.addedChildren[index]);
        this.addedChildren.splice(index, 1);
        this.validateChildrenAdded();
    }

    addChild(index: number) {
        this.addedChildren.push(this.children[index]);
        this.children.splice(index, 1);
        this.validateChildrenAdded();
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
                    let exercisesTemp = this.exercises;
                    let exercises1 = this.exercises.slice(0, i);
                    let exercises2 = this.exercises.slice(i + 1, size);
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

    goBack() {
        this.location.back();
    }
}
