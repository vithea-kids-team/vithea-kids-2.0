import { Component, Input, OnInit } from '@angular/core';
import { Exercise } from '../../../models/exercise';


@Component({
  selector: 'app-show-exercise-mc-image',
  templateUrl: './show-exercise-mc-image.component.html',
  styleUrls: ['./show-exercise-mc-image.component.css']
})
export class ShowExerciseMultipleChoiceImageComponent implements OnInit {

  @Input() exercise;

  public questionDescription;
  public stimulusText;
  public rightAnswer;
  public answers;
  public topic;
  public level;
  public resourcePathInit;
  public type = '';
  public development = false;

  constructor() {}

  ngOnInit() {
    this.questionDescription = this.exercise.question.questionDescription;
    this.stimulusText = this.exercise.question.stimulusText;
    this.rightAnswer = this.exercise.rightAnswer;
    if (this.development) {
      this.resourcePathInit = 'vithea-kids/assets/';
    }else {
      this.resourcePathInit = 'https://vithea.l2f.inesc-id.pt/';
    }
    this.answers = this.exercise.answers;
    this.topic = this.exercise.topic.topicDescription;
    this.level = this.exercise.level.levelDescription;
    this.type = this.exercise.type.toLowerCase();
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
}
