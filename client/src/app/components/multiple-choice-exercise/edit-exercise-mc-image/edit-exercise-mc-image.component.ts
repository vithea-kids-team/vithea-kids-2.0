import { Component, OnInit, OnChanges } from '@angular/core';
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
  selector: 'app-edit-exercise-mc-image',
  templateUrl: './edit-exercise-mc-image.component.html',
  styleUrls: ['./edit-exercise-mc-image.component.css']
})
export class EditExerciseMultipleChoiceImageComponent implements OnInit {

  public editExercise = new Exercise();
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

  public rightAnswerImgs1 = [];
  public rightAnswerImgs2 = [];
  public rightAnswerImgs3 = [];
  public distractorImgs1 = [];
  public distractorImgs2 = [];
  public distractorImgs3 = [];
  public exerciseId;
  public exerciseName;
  public topic;
  public level;
  public question;
  public numRightAnswers = 1;
  public numDistractors = 1;
  public stimulus;
  public distractors = [];
  public rightAnswers = [];
  public rightAnswerImg1;
  public rightAnswerImg2;
  public rightAnswerImg3;
  public distractorImg1;
  public distractorImg2;
  public distractorImg3;

  constructor(public modal: Modal, public route: ActivatedRoute, public resourcesService: ResourcesService,
    public exercisesService: ExercisesService, public router: Router, public location: Location,
    public utilsService: UtilsExercisesService) {}

    validateTopic() {
      this.topicError = this.utilsService.validateTopic(this.topic);
    }
    validateLevel() {
      this.levelError = this.utilsService.validateLevel(this.level);
    }
    validateQuestion() {
      this.questionError = this.utilsService.validateQuestion(this.question);
    }
    validateRightAnswerImage() {
      this.rightAnswerImageError = false;
      /*
        if (this.number < 2 ) {
          this.number++;
        } else {
          const rightAnswer = this.rightAnswerImgs.filter((rAnswer) => { return rAnswer.selected; });
          this.rightAnswerImageError = this.utilsService.validateRightAnswerImage(rightAnswer);
       }*/
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
        let temp = this.resourcesService.getResourcesByType('stimuli');

        this.rightAnswerImgs1 = JSON.parse(JSON.stringify(temp));
        this.rightAnswerImgs2 = JSON.parse(JSON.stringify(temp));
        this.rightAnswerImgs3 = JSON.parse(JSON.stringify(temp));
        this.distractorImgs1 = JSON.parse(JSON.stringify(temp));
        this.distractorImgs2 = JSON.parse(JSON.stringify(temp));
        this.distractorImgs3 = JSON.parse(JSON.stringify(temp));

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

    this.editExercise.type = 'image';
    this.editExercise.answers = [];
    this.editExercise.distractors = [];
    this.editExercise.rightAnswers = [];

    this.route.params
    .switchMap((params: Params) => Observable.of(params))
    .subscribe(params => {
      const sequenceId: number = parseInt(params['sequenceid'], 10);
      const exerciseId: number = parseInt(params['exerciseid'], 10);
      if (sequenceId) {
        this.editExercise.sequenceId = sequenceId;
      } else if (exerciseId) {
        this.exercisesService.getExercise(exerciseId).subscribe(
          (res: any) => {
            this.exerciseId = res.exerciseId;
            this.exerciseName = res.name;
            this.type = res.type.toLowerCase();
            this.topic = res.topic.topicId;
            this.level = res.level.levelId;
            this.question = res.question.questionDescription;
            this.stimulus = res.question.stimulusText;

            res.answers.forEach(element => {
              if (element.rightAnswer) {
                this.rightAnswers.push(element);
              } else {
                this.distractors.push(element);
              }
            });

            this.numRightAnswers = this.rightAnswers.length;
            switch (this.numRightAnswers) {
              case 1: {
                this.rightAnswerImg1 = this.rightAnswers[0].stimulus;
                break;
              } case 2: {
                this.rightAnswerImg1 = this.rightAnswers[0].stimulus;
                this.rightAnswerImg2 = this.rightAnswers[1].stimulus;
                break;
              } case 3: {
                this.rightAnswerImg1 = this.rightAnswers[0].stimulus;
                this.rightAnswerImg2 = this.rightAnswers[1].stimulus;
                this.rightAnswerImg3 = this.rightAnswers[2].stimulus;
                break;
              }
            }

            this.numDistractors = this.distractors.length;
            switch (this.numDistractors) {
              case 1: {
                this.distractorImg1 = this.distractors[0].stimulus;
                break;
              } case 2: {
                this.distractorImg1 = this.distractors[0].stimulus;
                this.distractorImg2 = this.distractors[1].stimulus;
                break;
              } case 3: {
                this.distractorImg1 = this.distractors[0].stimulus;
                this.distractorImg2 = this.distractors[1].stimulus;
                this.distractorImg3 = this.distractors[2].stimulus;
                break;
              }
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

    this.editExercise.exerciseId = this.exerciseId;
    this.editExercise.topic = this.topic;
    this.editExercise.level = this.level;
    this.editExercise.question = this.question;
    if (this.stimulus !== '') {
      this.editExercise.stimulusText = this.stimulus;
    } else {
      this.editExercise.stimulusText = null;
    }

    const rightAnswer1 = this.rightAnswerImgs1.filter((rAnswer) => { return rAnswer.selected; });
    const rightAnswer2 = this.rightAnswerImgs2.filter((rAnswer) => { return rAnswer.selected; });
    const rightAnswer3 = this.rightAnswerImgs3.filter((rAnswer) => { return rAnswer.selected; });
    const distractor1 = this.distractorImgs1.filter((stimulus) => { return stimulus.selected; });
    const distractor2 = this.distractorImgs2.filter((stimulus) => { return stimulus.selected; });
    const distractor3 = this.distractorImgs3.filter((stimulus) => { return stimulus.selected; });

    if (rightAnswer1.length === 1) {
      this.editExercise.rightAnswers.push(rightAnswer1[0].resourceId);
    }
    if (rightAnswer2.length === 1) {
      this.editExercise.rightAnswers.push(rightAnswer2[0].resourceId);
    }
    if (rightAnswer3.length === 1) {
      this.editExercise.rightAnswers.push(rightAnswer3[0].resourceId);
    }

    if (distractor1.length === 1) {
      this.editExercise.distractors.push(distractor1[0].resourceId);
    }
    if (distractor2.length === 1) {
      this.editExercise.distractors.push(distractor2[0].resourceId);
    }
    if (distractor3.length === 1) {
      this.editExercise.distractors.push(distractor3[0].resourceId);
    }

      const dialogRef = this.modal.confirm().size('lg').isBlocking(true).showClose(false).okBtn('Sim').cancelBtn('Não')
      .title('Editar exercício').body(`Tem a certeza que pretende editar o exercício?`).open();

      dialogRef.then(dialogRef2 => { dialogRef2.result.then(result => {
        if (result) {
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
            }
          );
        } else {
          this.goBack();
        }
      }).catch(() => {});
    });
  }

  addRightAnswer() {
    this.numRightAnswers++;
  }
  addDistractor() {
    this.numDistractors++;
  }

  goBack() {
    this.location.back();
  }
}
