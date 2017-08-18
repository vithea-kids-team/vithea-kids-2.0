import 'rxjs/add/operator/switchMap';

import { Component, OnInit, OnChanges } from '@angular/core';
import { ActivatedRoute, Params }   from '@angular/Router';
import { Observable } from 'rxjs/Observable';
import { Location } from '@angular/common';
import { Exercise } from '../../models/exercise';
import { ExercisesService } from '../../services/exercises/exercises.service';
import { ChildrenService } from '../../services/children/children.service';
import { PaginationService } from '../../services/pagination/pagination.service';

@Component({
  selector: 'app-exercises',
  templateUrl: './exercises.component.html',
  styleUrls: ['./exercises.component.css']
})
export class ExercisesComponent implements OnInit, OnChanges {

  public exercises: Array<Exercise>;
  public sequenceId: number = 0;
  public sequence;
  public loading: boolean = false;

  // pager object
  pager: any = {};

  // paged items
  pagedItems: any[];

  public textFilter = true;
  public imageFilter = true;

  constructor(public route: ActivatedRoute, public exercisesService: ExercisesService,
    public childrenService: ChildrenService, public paginationService: PaginationService,
    public location: Location) { }

  ngOnInit() {
    this.fetchExercises();
  }

  ngOnChanges() {
    this.fetchExercises();
  }

  public fetchExercises() {
    this.loading = true;
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
        this.exercises = res.json().sequenceExercises;
        this.setPage(1);
        this.loading = false;
      },
      err => {
        console.error('Error loading sequence exercises.' + err);
        this.exercises = [];
      }
    );
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
    this.loading = true;
    this.exercisesService.deleteExercise(exerciseId).subscribe(
      result => this.fetchExercises(),
      err => {
        console.log('Error deleting exercise');
        this.loading = false;
      }
    )
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

  goBack() {
    this.location.back();
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
