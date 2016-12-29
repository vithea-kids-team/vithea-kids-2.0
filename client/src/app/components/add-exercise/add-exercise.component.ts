import { Component, OnInit } from '@angular/core';
import { Exercise } from '../../models/exercise';
import { Resource } from '../../models/resource';
import { ResourcesService } from '../../services/resources/resources.service';

@Component({
  selector: 'app-add-exercise',
  templateUrl: './add-exercise.component.html',
  styleUrls: ['./add-exercise.component.css']
})
export class AddExerciseComponent implements OnInit {

  public newExercise = new Exercise();
  public newAnswer : string = '';

  constructor(private resourcesService: ResourcesService) {}

  ngOnInit() {
    this.newExercise.type = 'text';
    this.newExercise.answers = [];
    this.newExercise.rightAnswers = [];
    this.newExercise.stimulus = [];
    this.newExercise.answersImg = [];
  }

  addAnswer() {
    if(this.newAnswer !== '') {
      this.newExercise.answers.push(this.newAnswer);
      this.newAnswer = '';
    }
  }

  removeAnswer(index : number) {
    this.newExercise.answers.splice(index, 1);
  }

  registerExercise() {
    console.log(this.newExercise);
  }

}
