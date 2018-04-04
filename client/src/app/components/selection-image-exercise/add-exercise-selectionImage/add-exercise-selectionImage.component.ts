import { SelectionArea } from './../../../models/selectionArea';
import { Component, OnInit, OnChanges, ViewChild, ElementRef } from '@angular/core';
import { ActivatedRoute, Params }   from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { Router } from '@angular/router';
import { Exercise } from '../../../models/exercise';

import { Resource } from '../../../models/resource';
import { ResourcesService } from '../../../services/resources/resources.service';
import { ExercisesService } from '../../../services/exercises/exercises.service';
import { UtilsExercisesService} from '../../../services/utils/utils-exercises.service';

import { Overlay } from 'ngx-modialog';
import { Modal } from 'ngx-modialog/plugins/bootstrap';
import { Location } from '@angular/common';

import { SimpleChanges } from '@angular/core/src/metadata/lifecycle_hooks';

@Component({
  selector: 'app-add-exercise-selectionImage',
  templateUrl: './add-exercise-selectionImage.component.html',
  styleUrls: ['./add-exercise-selectionImage.component.css']
})
export class AddExerciseSelectionImageComponent implements OnInit {
  public newExercise = new Exercise();
  @ViewChild('imageCanvas') imageCanvas: ElementRef;
  public context: CanvasRenderingContext2D;
  public rect = {};
  public topics = [];
  public levels = [];
  public error: string = undefined;
  public loading = true;
  public question;
  public images = [];
  public imageSelected;
  public questionError;
  public topicError;
  public levelError;
  public imageError;
  public selectionError;

  public selectionArea = new SelectionArea();
  public topic;
  public level;
  public drag = false;

  public imageUrl;
  public development = true;

  constructor(public modal: Modal, public route: ActivatedRoute, public resourcesService: ResourcesService,
    public exercisesService: ExercisesService, public router: Router, public location: Location,
    public utilsService: UtilsExercisesService) {}

  ngOnInit() {
    this.loading = true;
    this.resourcesService.fetchResources().subscribe(
      resResources => {
        let temp = this.resourcesService.getResourcesByType('stimuli');
        this.images = temp;
        this.resourcesService.fetchLevels().subscribe(
          resLevels => {
              this.levels = resLevels;
              this.resourcesService.fetchTopics().subscribe(
                resTopics => {
                    this.topics = resTopics;
                })
                this.loading = false;
          })
        this.loading = false;
      })
      this.newExercise.type = 'selectionImage';
      this.newExercise.selectionsAreas = [];
  }


  getImage(event) {
    if (event === 'toggleItems' ) {
        this.imageSelected = this.images.filter(image =>  image.selected === true);
        if (this.development) {
          this.imageUrl = 'vithea-kids/assets/' +  this.imageSelected[0].resourcePath;
        }else {
          this.imageUrl = 'https://vithea.l2f.inesc-id.pt/' +  this.imageSelected[0].resourcePath;
        }
        this.imageCanvas.nativeElement.style['background-image'] = 'url(' + this.imageUrl + ')';
   }
  }

  mouseDown(e) {
    this.context = this.imageCanvas.nativeElement.getContext('2d');
    let canvasTop = this.imageCanvas.nativeElement.getBoundingClientRect().top;
    let canvasLeft = this.imageCanvas.nativeElement.getBoundingClientRect().left;
    this.selectionArea.startX = e.clientX - canvasLeft;
    this.selectionArea.startY = e.clientY - canvasTop;
    this.drag = true;
  }

  mouseUpEvent(e) {
    this.drag = false;
  }

  mouseMove(e) {
    if (this.drag) {
      this.context.clearRect(0, 0, 400, 400);
      let canvasTop = this.imageCanvas.nativeElement.getBoundingClientRect().top;
      let canvasLeft = this.imageCanvas.nativeElement.getBoundingClientRect().left;
      this.selectionArea.endX = e.clientX - canvasLeft - this.selectionArea.startX;
      this.selectionArea.endY = e.clientY - canvasTop - this.selectionArea.startY;
      // draw rect
      this.context.setLineDash([6]);
      this.context.globalAlpha = 0.25;
      this.context.fillRect(this.selectionArea.startX , this.selectionArea.startY , this.selectionArea.endX, this.selectionArea.endY);
    }
  }

  clearSelection() {
    this.context.clearRect(0, 0, 400, 400);
  }


  // adapt this to multiple areas
  registerSelectionImageExercise() {
    this.newExercise.topic = this.topic;
    this.newExercise.level = this.level;
    this.newExercise.question = this.question;
    this.newExercise.selectionsAreas.push(this.selectionArea);
    this.newExercise.stimulus = this.imageSelected[0].resourceId;
    this.newExercise.widthOriginal = this.imageCanvas.nativeElement.width;
    this.newExercise.heightOriginal = this.imageCanvas.nativeElement.height;

    const dialogRef = this.modal.confirm().size('lg').isBlocking(true).showClose(false).okBtn('Sim').cancelBtn('Não')
    .title('Registar exercício').body('Tem a certeza que pretende registar o exercício?').open();

    dialogRef.then(dialogRef2 => { dialogRef2.result.then(result => {
      if (result) {
        this.exercisesService.addExercise(this.newExercise).subscribe(
          res => {
            if (this.newExercise.sequenceId) {
              this.router.navigate(['/sequences' + this.newExercise.sequenceId]);
            } else {
              this.router.navigate(['/exercises']);
            }
            this.exercisesService.setSuccess(true);
            this.exercisesService.setFailure(false);
            this.exercisesService.setTextSuccess('Exercício registado com sucesso.');
          },
          err => {
            console.error('Error registering new exercise.', err);
            this.exercisesService.setSuccess(false);
            this.exercisesService.setFailure(true);
            this.exercisesService.setTextFailure('Não foi possível registar o exercício.');
          }
        );
      } else {
        this.goBack();
      }
    }).catch(() => {})});
  }

  submit () {
    this.validateTopic();
    this.validateLevel();
    this.validateQuestion();
    this.validateStimulus();
    this.valideSelectionArea();


    if (this.topicError === false && this.levelError === false && this.questionError === false && this.imageError === false
    && this.selectionError === false) {
          this.registerSelectionImageExercise();
    }
  }

  validateQuestion() {
    this.questionError = this.utilsService.validateQuestion(this.question);
  }
  validateTopic() {
    this.topicError = this.utilsService.validateTopic(this.topic);
  }
  validateLevel() {
    this.levelError = this.utilsService.validateLevel(this.level);
  }
  validateStimulus() {
    if (this.imageSelected === undefined) {
      this.imageError = true;
    } else {
      this.imageError = false;
    }

  }

  valideSelectionArea() {
    if (this.selectionArea.startX === undefined || this.selectionArea.startY === undefined || this.selectionArea.endX === undefined 
      || this.selectionArea.endY === undefined) {
        this.selectionError = true;
    }else {
      this.selectionError = false;
    }
  }


  goBack() {
    this.location.back();
  }

}
