import { SelectionArea } from './../../../models/selectionArea';
import { Component, OnInit, ViewChild, ElementRef, Renderer, AfterViewChecked} from '@angular/core';
import { ActivatedRoute, Params }   from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { Router } from '@angular/router';
import { Exercise } from '../../../models/exercise';
import { Resource } from '../../../models/resource';
import { ResourcesService } from '../../../services/resources/resources.service';
import { ExercisesService } from '../../../services/exercises/exercises.service';
import { Overlay } from 'ngx-modialog';
import { Modal } from 'ngx-modialog/plugins/bootstrap';
import { Location } from '@angular/common';
import { UtilsExercisesService} from '../../../services/utils/utils-exercises.service';


@Component({
  selector: 'app-edit-exercise-selectionImage',
  templateUrl: './edit-exercise-selectionImage.component.html',
  styleUrls: ['./edit-exercise-selectionImage.component.css']
})

export class EditExerciseSelectionImageComponent implements OnInit, AfterViewChecked {


  public editExercise = new Exercise();
  public loading = false;
  public images = [];
  public topics = [];
  public levels = [];
  public imageSelected;
  public imageUrl;

  public context: CanvasRenderingContext2D;
  public drag = false;

  public questionDescription;
  public stimulus;
  public rightAnswer;
  public answers;
  public topic;
  public level;
  public resourcePath;
  public type = 'selectionImage';
  public development = false;
  public error;
  public exerciseId: number;

  public dataRetrieved = false;
  public selectionArea: SelectionArea = new SelectionArea();
  @ViewChild('imageCanvas') imageCanvas: any;


  constructor(public modal: Modal, public route: ActivatedRoute, public resourcesService: ResourcesService,
    public exercisesService: ExercisesService, public router: Router, public location: Location,
    public utilsService: UtilsExercisesService, private renderer: Renderer) {}

  ngOnInit() {
    this.loading = true;

    this.resourcesService.fetchResources().subscribe(
      resResources => {
        this.images  = this.resourcesService.getResourcesByType('stimuli');
        this.resourcesService.fetchLevels().subscribe(
          resLevels => {
              this.levels = resLevels;
              this.resourcesService.fetchTopics().subscribe(
                resTopics => {
                    this.topics = resTopics;
                    this.loading = false;
                })
          })
      });

     this.route.params
    .switchMap((params: Params) => Observable.of(params))
    .subscribe(params => {
      const sequenceId: number = parseInt(params['sequenceid'], 10);
      this.exerciseId = parseInt(params['exerciseid'], 10);
      if (sequenceId) {
        this.editExercise.sequenceId = sequenceId;
      } else if (this.exerciseId) {
        this.exercisesService.getExercise(this.exerciseId).subscribe(
          (res: any) => {
           this.editExercise.question = res.question1;
           this.editExercise.level = res.level.levelId;
           this.editExercise.topic = res.topic.topicId;
           this.editExercise.stimulus = res.stimulus.resourcePath;
           this.editExercise.selectionsAreas = res.selectionAreas;
           this.dataRetrieved = true;
          },
          err => {
            console.error('Error getting exercise.', err);
            this.loading = false;
          }
        );

      }
    });
    
    if (this.development) {
      this.editExercise.stimulus = 'vithea-kids/assets/' + this.editExercise.stimulus;
    } else {
      this.editExercise.stimulus = 'https://vithea.l2f.inesc-id.pt/' + this.editExercise.stimulus;
    }
  }

  ngAfterViewChecked() {
    if (this.dataRetrieved) {
      this.drawRectangleImage();
      this.dataRetrieved = false;
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
    this.imageCanvas.nativeElement.style['background-image'] = 'url(' + 'vithea-kids/assets/' + this.editExercise.stimulus + ')';
    this.selectionArea.startX = this.editExercise.selectionsAreas[0].startX;
    this.selectionArea.endX =  this.editExercise.selectionsAreas[0].endX;
    this.selectionArea.startY = this.editExercise.selectionsAreas[0].startY;
    this.selectionArea.endY = this.editExercise.selectionsAreas[0].endY;
    this.context = this.imageCanvas.nativeElement.getContext('2d');
    this.context.setLineDash([6]);
    this.context.fillStyle = '#1d921d';
    this.context.globalAlpha = 0.25;
    this.context.fillRect(this.selectionArea.startX , this.selectionArea.startY , this.selectionArea.endX, this.selectionArea.endY);
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

  editSelectionExercise() {
    this.error = undefined;
    this.editExercise.selectionsAreas.length = 0;
    this.editExercise.selectionsAreas.push(this.selectionArea);
    this.editExercise.type = 'selectionImage';
    this.editExercise.exerciseId = this.exerciseId;

    const stimulus = this.images.filter((stimulus2) => { return stimulus2.selected; });
    if (stimulus.length === 1) {
      this.editExercise.stimulus = stimulus[0].resourceId;
    } else {
      this.editExercise.stimulus = null;
    }


    const dialogRef = this.modal.confirm().size('lg').isBlocking(true).showClose(false).okBtn('Sim').cancelBtn('Não')
    .title('Editar exercício').body(`Tem a certeza que pretende editar o exercício?`).open();

    dialogRef.then(dialogRef2 => { dialogRef2.result.then(result => {
      if (result) {
        console.log(this.selectionArea);
        console.log(this.editExercise);
        this.exercisesService.editExercise(this.editExercise).subscribe(
          res => {
            this.router.navigate(['/exercises']);
            this.exercisesService.setSuccess(true);
            this.exercisesService.setFailure(false);
            this.exercisesService.setTextSuccess('Exercício editado com sucesso.');
          },
          err => {
            this.error = err._body;
            console.error('Error editing the exercise.', err);
            this.exercisesService.setSuccess(false);
            this.exercisesService.setFailure(true);
            this.exercisesService.setTextFailure('Não foi possível editar o exercício.');
          });
        } else {
          this.goBack();
        }
      }).catch(() => {});
    });
  }


  goBack() {
    this.location.back();
  }
}
