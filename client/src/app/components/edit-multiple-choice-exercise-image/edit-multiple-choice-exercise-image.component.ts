import { Component, OnInit, OnChanges } from '@angular/core';
import { ActivatedRoute, Params }   from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { Router } from '@angular/router';
import { Exercise } from '../../models/exercise';
import { Resource } from '../../models/resource';
import { ResourcesService } from '../../services/resources/resources.service';
import { ExercisesService } from '../../services/exercises/exercises.service';
import { Overlay } from 'ngx-modialog';
import { Modal } from 'ngx-modialog/plugins/bootstrap';
import { Location } from '@angular/common';
import { UtilsExercisesService} from '../../services/utils/utils-exercises.service';

@Component({
  selector: 'app-edit-multiple-choice-exercise-image',
  templateUrl: './edit-multiple-choice-exercise-image.component.html',
  styleUrls: ['./edit-multiple-choice-exercise-image.component.css']
})
export class EditMultipleChoiceExerciseImageComponent implements OnInit {

  public newExercise = new Exercise();
  public question = '';
  public rightAnswerImgs = [];
  public answersImgs1 = [];
  public answersImgs2 = [];
  public answersImgs3 = [];
  public topics = [];
  public levels = [];
  public error: string = undefined;
  public loading = false;
  public type = '';

  public rightAnswerTextError;
  public rightAnswerImageError;
  public topicError;
  public levelError;
  public questionError;

  public number = 0;

  constructor(public modal: Modal, public route: ActivatedRoute, public resourcesService: ResourcesService,
    public exercisesService: ExercisesService, public router: Router, public location: Location,
    public utilsService: UtilsExercisesService) {}

    validateTopic() {
      this.topicError = this.utilsService.validateTopic(this.newExercise.topic);
    }
    validateLevel() {
      this.levelError = this.utilsService.validateLevel(this.newExercise.level);
    }
    validateQuestion() {
      this.questionError = this.utilsService.validateQuestion(this.newExercise.question);
    }
    validateRightAnswerImage() {
        if (this.number < 2 ) {
          this.number++;
        } else {
          const rightAnswer = this.rightAnswerImgs.filter((rAnswer) => { return rAnswer.selected; });
          this.rightAnswerImageError = this.utilsService.validateRightAnswerImage(rightAnswer);
       }
    }

  submit () {
    this.validateTopic();
    this.validateLevel();
    this.validateQuestion();
    this.validateRightAnswerImage();

    if (this.topicError === false && this.levelError === false && this.questionError === false &&
      this.rightAnswerImageError === false) {
      this.editMultipleChoiceImageExercise();
    }
  }

  ngOnInit() {
    this.loading = true;
    this.resourcesService.fetchResources().subscribe(
      resResources => {
        this.rightAnswerImgs = this.resourcesService.getResourcesByType('stimuli');
        this.answersImgs1 = this.resourcesService.getResourcesByType('stimuli');
        this.answersImgs2 = this.resourcesService.getResourcesByType('stimuli');
        this.answersImgs3 = this.resourcesService.getResourcesByType('stimuli');
        this.loading = false;

        this.resourcesService.fetchLevels().subscribe(
          resLevels => {
              this.levels = resLevels;
              this.resourcesService.fetchTopics().subscribe(
                resTopics => {
                    this.topics = resTopics;
                    this.loading = false;
                }
            )
          }
        )
      }
    )
    this.loading = false;

    this.newExercise.type = 'image';
    this.newExercise.answersImg = [];

    this.route.params
    .switchMap((params: Params) => Observable.of(params))
    .subscribe(params => {
      const sequenceId: number = parseInt(params['sequenceid'], 10);
      const exerciseId: number = parseInt(params['exerciseid'], 10);
      if (sequenceId) {
        this.newExercise.sequenceId = sequenceId;
        this.loading = false;
      } else if (exerciseId) {
        this.exercisesService.getExercise(exerciseId).subscribe(
          (res: any) => {
            this.newExercise.exerciseId = res.exerciseId;
            this.newExercise.exerciseName = res.name;
            this.newExercise.type = res.type.toLowerCase();
            this.newExercise.topic = res.topic.topicId;
            this.newExercise.level = res.level.levelId;
            this.newExercise.question = res.question.questionDescription;
            this.newExercise.stimulusText = res.question.stimulusText;
            this.newExercise.rightAnswer = res.rightAnswer.stimulus;
            if (res.answers.length === 2) {
              this.newExercise.distractor1 = res.answers[1].stimulus;
            } else if (res.answers.length === 3) {
              this.newExercise.distractor1 = res.answers[1].stimulus
              this.newExercise.distractor2 = res.answers[2].stimulus
            } else if (res.answers.length === 4) {
              this.newExercise.distractor1 = res.answers[1].stimulus
              this.newExercise.distractor2 = res.answers[2].stimulus
              this.newExercise.distractor3 = res.answers[3].stimulus
            }
          },
          err => {
            console.error('Error getting exercise.', err);
            this.loading = false;
        }
        );
      }
    });
  }

  editMultipleChoiceImageExercise() {
    this.error = undefined;
      const rightAnswer = this.rightAnswerImgs.filter((rAnswer) => { return rAnswer.selected; });
      const answersImg1 = this.answersImgs1.filter((stimulus) => { return stimulus.selected; });
      const answersImg2 = this.answersImgs2.filter((stimulus) => { return stimulus.selected; });
      const answersImg3 = this.answersImgs3.filter((stimulus) => { return stimulus.selected; });

      if (rightAnswer.length > 0) {
        this.newExercise.rightAnswerImg = rightAnswer[0].resourceId;
      }
      if (answersImg1.length === 1) {
        this.newExercise.answersImg.push(answersImg1[0].resourceId);
      }
      if (answersImg2.length === 1) {
        this.newExercise.answersImg.push(answersImg2[0].resourceId);
      }
      if (answersImg3.length === 1) {
        this.newExercise.answersImg.push(answersImg3[0].resourceId);
      }

      const dialogRef = this.modal.confirm().size('lg').isBlocking(true).showClose(false).okBtn('Sim').cancelBtn('Não')
      .title('Editar exercício').body(`Tem a certeza que pretende editar o exercício?`).open();

      dialogRef.then(dialogRef2 => { dialogRef2.result.then(result => {
        if (result) {
          this.exercisesService.editExercise(this.newExercise).subscribe(
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
            }
          );
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
