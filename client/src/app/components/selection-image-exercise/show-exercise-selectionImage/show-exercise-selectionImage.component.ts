import { SelectionArea } from './../../../models/selectionArea';
import { Component, Input, OnInit, ViewChild, ElementRef  } from '@angular/core';
import { Exercise } from '../../../models/exercise';


@Component({
  selector: 'app-show-exercise-selectionImage',
  templateUrl: './show-exercise-selectionImage.component.html',
  styleUrls: ['./show-exercise-selectionImage.component.css']
})
export class ShowExerciseSelectionImageComponent implements OnInit {

  @Input() exercise;

  public questionDescription;
  public stimulus;
  public rightAnswer;
  public answers;
  public topic;
  public level;
  public resourcePath;
  public type = 'selectionImage';
  public development = true;
  public context: CanvasRenderingContext2D;
  public selectionArea: SelectionArea = new SelectionArea();
  @ViewChild('imageCanvas') imageCanvas: ElementRef;



  constructor() {}

  ngOnInit() {
    this.questionDescription = this.exercise.question1;
    console.log(this.exercise);
    this.stimulus = this.exercise.stimulus;
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
    this.type = this.exercise.dtype.toLowerCase();
    console.log((this.exercise.selectionAreas[0].endX * 100) / this.exercise.widthOriginal);
    this.drawRectangleImage();
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


  public drawRectangleImage() {
    this.imageCanvas.nativeElement.style['background-image'] = 'url(' + this.resourcePath + ')';
    this.selectionArea.startX = (100 / this.exercise.widthOriginal)  * this.exercise.selectionAreas[0].startX;
    this.selectionArea.endX = (100 / this.exercise.widthOriginal) * this.exercise.selectionAreas[0].endX;
    this.selectionArea.startY = (100 / this.exercise.heightOriginal) * this.exercise.selectionAreas[0].startY;
    this.selectionArea.endY = (100 / this.exercise.heightOriginal) * this.exercise.selectionAreas[0].endY;
    this.context = this.imageCanvas.nativeElement.getContext('2d');
    this.context.setLineDash([6]);
    this.context.fillStyle = '#1d921d';
    this.context.globalAlpha = 0.25;
    this.context.fillRect(this.selectionArea.startX , this.selectionArea.startY , this.selectionArea.endX, this.selectionArea.endY);
  }
}
