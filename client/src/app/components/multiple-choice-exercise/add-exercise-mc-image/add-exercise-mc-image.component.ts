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
  selector: 'app-add-exercise-mc-image',
  templateUrl: './add-exercise-mc-image.component.html',
  styleUrls: ['./add-exercise-mc-image.component.css']
})
export class AddExerciseMultipleChoiceImageComponent implements OnInit {

  public newExercise = new Exercise();
  public rightAnswerImgs1 = [];
  public rightAnswerImgs2 = [];
  public rightAnswerImgs3 = [];
  public distractorImgs1 = [];
  public distractorImgs2 = [];
  public distractorImgs3 = [];
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


 // temp vars
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
          this.registerMultipleChoiceImageExercise();
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
                })
          })
      })
    this.loading = false;
    this.newExercise.answers = [];
    this.newExercise.rightAnswers = [];
    this.newExercise.distractors = [];
    this.newExercise.type = 'image';
  }

  registerMultipleChoiceImageExercise() {
      this.error = undefined;

      this.newExercise.topic = this.topic;
      this.newExercise.level = this.level;
      this.newExercise.question = this.question;
      if (this.stimulus !== '') {
        this.newExercise.stimulusText = this.stimulus;
      } else {
        this.newExercise.stimulusText = null;
      }

      const rightAnswer1 = this.rightAnswerImgs1.filter((rAnswer) => { return rAnswer.selected; });
      const rightAnswer2 = this.rightAnswerImgs2.filter((rAnswer) => { return rAnswer.selected; });
      const rightAnswer3 = this.rightAnswerImgs3.filter((rAnswer) => { return rAnswer.selected; });
      const distractor1 = this.distractorImgs1.filter((stimulus) => { return stimulus.selected; });
      const distractor2 = this.distractorImgs2.filter((stimulus) => { return stimulus.selected; });
      const distractor3 = this.distractorImgs3.filter((stimulus) => { return stimulus.selected; });

      if (rightAnswer1.length === 1) {
        this.newExercise.rightAnswers.push(rightAnswer1[0].resourceId);
      }
      if (rightAnswer2.length === 1) {
        this.newExercise.rightAnswers.push(rightAnswer2[0].resourceId);
      }
      if (rightAnswer3.length === 1) {
        this.newExercise.rightAnswers.push(rightAnswer3[0].resourceId);
      }

      if (distractor1.length === 1) {
        this.newExercise.distractors.push(distractor1[0].resourceId);
      }
      if (distractor2.length === 1) {
        this.newExercise.distractors.push(distractor2[0].resourceId);
      }
      if (distractor3.length === 1) {
        this.newExercise.distractors.push(distractor3[0].resourceId);
      }

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
            this.newExercise.answers = [];
          }
        );
      } else {
        this.goBack();
      }
    }).catch(() => {})});
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
