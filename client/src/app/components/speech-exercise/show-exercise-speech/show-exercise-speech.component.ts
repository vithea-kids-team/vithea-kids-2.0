import { Component, Input, OnInit } from '@angular/core';
import { Exercise } from '../../../models/exercise';


@Component({
  selector: 'app-show-exercise-speech',
  templateUrl: './show-exercise-speech.component.html',
  styleUrls: ['./show-exercise-speech.component.css']
})
export class ShowExerciseSpeechComponent implements OnInit {

  @Input() exercise;

  public questionDescription;
  public stimulus;
  public rightAnswer;
  public answers;
  public topic;
  public level;
  public resourcePath;
  public type = '';
  public development = true;

  constructor() {}

  ngOnInit() {

    this.questionDescription = this.exercise.questionSpeech;
    this.stimulus = this.exercise.stimulusSpeech;
    if (this.stimulus) {
      this.resourcePath = this.stimulus.resourcePath;
      if (this.development) {
        this.resourcePath = 'vithea-kids/assets/' + this.stimulus.resourcePath;
      } else {
        this.resourcePath = 'https://vithea.l2f.inesc-id.pt/' + this.stimulus.resourcePath;
      }
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
