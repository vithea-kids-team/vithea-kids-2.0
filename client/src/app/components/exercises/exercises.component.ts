import { Component, OnInit } from '@angular/core';
import { Exercise } from '../../models/Exercise'
import { ExercisesService } from '../../services/exercises/exercises.service'

@Component({
  selector: 'app-exercises',
  templateUrl: './exercises.component.html',
  styleUrls: ['./exercises.component.css']
})
export class ExercisesComponent implements OnInit {

  private exercises: Array<Exercise>
  private subs = []

  constructor(private exercisesService : ExercisesService) { }

  ngOnInit() {
    this.getExercises()
  }

  private getExercises() {
    this.subs.push(this.exercisesService.getExercises().subscribe(
      result => this.exercises = result,
      err => console.error("Error loading exercises. " + err) 
    ))
  }

  ngOnDestroy() {
    this.subs.forEach(subscription => {
      subscription.unsubscribe()
    });
  }
}
