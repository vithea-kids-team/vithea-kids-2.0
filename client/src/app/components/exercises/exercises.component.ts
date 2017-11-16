import 'rxjs/add/operator/switchMap';

import { Component, OnInit, OnChanges } from '@angular/core';
import { ActivatedRoute, Params }   from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { Location } from '@angular/common';
import { Exercise } from '../../models/exercise';
import { ExercisesService } from '../../services/exercises/exercises.service';
import { ChildrenService } from '../../services/children/children.service';
import { PaginationService } from '../../services/pagination/pagination.service';
import { Overlay } from 'ngx-modialog';
import { Modal } from 'ngx-modialog/plugins/bootstrap';

@Component({
  selector: 'app-exercises',
  templateUrl: './exercises.component.html',
  styleUrls: ['./exercises.component.css']
})
export class ExercisesComponent implements OnInit, OnChanges {

  public exercises: Array<Exercise>;
  public sequenceId= 0;
  public sequence;
  public loading = false;
  public success = false;
  public failure = false;
  public textSuccess;
  public textFailure;
  public development = true;

  // pager object
  pager: any = {};

  // paged items
  pagedItems: any[];

  public textFilter = true;
  public imageFilter = true;

  constructor(public route: ActivatedRoute, public exercisesService: ExercisesService,
    public childrenService: ChildrenService, public paginationService: PaginationService,
    public location: Location, public modal: Modal) { }

  ngOnInit() {
    this.fetchExercises();
  }

  ngOnChanges() {
    this.fetchExercises();
  }

  public updateSuccessFailure() {
    this.success = this.exercisesService.getSuccess();
    this.failure = this.exercisesService.getFailure();
    this.textSuccess = this.exercisesService.getTextSuccess();
    this.textFailure = this.exercisesService.getTextFailure();
  }


  public fetchExercises() {
    this.loading = true;
    this.updateSuccessFailure();

    this.route.params
      .switchMap((params: Params) => Observable.of(params))
      .subscribe(params => {
        const id: number = parseInt(params['sequenceid'], 10);
        if (id) {
          this.sequenceId = id;
          this.getSequenceExercises(id);
        } else {
          this.getExercises();
        }
      },
      err => {
        console.error('Error loading exercises', err);
        this.loading = false;
      });
  }

  public getSequenceExercises(id) {
    this.childrenService.getSequence(id).subscribe(
      res => {
        this.sequence = res.json();

        this.exercisesService.getExercises().subscribe(
          result => {
            this.exercises = result;
            let sequenceExercisesOrder = res.json().sequenceExercisesList;
            let exercises = [];
            sequenceExercisesOrder.forEach(exor => {
              let exercise = this.findExerciseById(exor.sequenceExerciseId.exercise_id);

              if (exercise !== null) {
                exercises.push(exercise);
              }
            });
            this.exercises = exercises;
            this.setPage(1);
            this.loading = false;
          },
          err => {
            console.error('Error loading exercises.' + err);
            this.exercises = [];
            this.loading = false;
          })
        },
      err => {
          console.error('Error loading sequence exercises.' + err);
          this.exercises = [];
          this.loading = false;
      });
    }

  findExerciseById(exerciseId: number) {
    let length = this.exercises.length;
    for (let i = 0; i < length; i++) {
        let exercise = this.exercises[i];
        if (exercise.exerciseId === exerciseId) {
            return exercise;
        }
    }
    return null;
}

  public getExercises() {
    this.exercisesService.getExercises().subscribe(
      result => {
        this.exercises = result;
        this.setPage(1);
        this.loading = false;
      },
      err => {
        console.error('Error loading sequence exercises.' + err);
        this.exercises = [];
      }
    );
  }

  public deleteExercise(exerciseId) {

    const dialogRef = this.modal.confirm().size('lg').isBlocking(true).showClose(false).okBtn('Sim').cancelBtn('Não')
    .title('Eliminar exercício').body(`Tem a certeza que pretende eliminar o exercício?`).open();

    dialogRef.then(dialogRef => { dialogRef.result.then(result => {
      if (result) {
        this.exercisesService.deleteExercise(exerciseId).subscribe(
          res => {
            this.exercisesService.setSuccess(true);
            this.exercisesService.setFailure(false);
            this.exercisesService.setTextSuccess('Exercício eliminado com sucesso.');
            this.fetchExercises();
          },
          err => {
            console.log('Error deleting exercise');
            this.exercisesService.setSuccess(false);
            this.exercisesService.setFailure(true);
            this.exercisesService.setTextFailure('Não foi possível eliminar o exercício.');
            this.updateSuccessFailure();
          }
        );
      } else {
        this.goBack();
      }
    }).catch(() => {})});
  }

  public addExercise(exercise) {
    this.loading = true;
    this.exercisesService.addExercise(exercise).subscribe(
      result => {
        this.loading = false;
        return result.json().exerciseId;
      },
      err => {
        console.error('Error adding exercise.' + err);
        this.loading = false;
      }
    )
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

  goBack() {
    this.location.back();
  }

  reset() {
    this.exercisesService.success = false;
    this.exercisesService.failure = false;
    this.exercisesService.textFailure = '';
    this.exercisesService.textSuccess = '';
  }

  setPage(page: number) {
    if (page < 1 || page > this.pager.totalPages) {
      return;
    }

    // get pager object from service
    this.pager = this.paginationService.getPager(this.exercises.length, page);

    // get current page of items
    this.pagedItems = this.exercises.slice(this.pager.startIndex, this.pager.endIndex + 1);
  }
}
