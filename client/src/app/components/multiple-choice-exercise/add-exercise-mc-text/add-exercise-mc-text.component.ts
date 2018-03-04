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
  selector: 'app-add-exercise-mc-text',
  templateUrl: './add-exercise-mc-text.component.html',
  styleUrls: ['./add-exercise-mc-text.component.css']
})
export class AddExerciseMultipleChoiceTextComponent implements OnInit {

  public newExercise = new Exercise();
  public newAnswer = '';
  public stimulusImgs = [];
  public topics = [];
  public levels = [];
  public error: string = undefined;
  public loading = false;
  public type = ''; // ?
  public rightAnswerTextError;
  public rightAnswerImageError;
  public topicError;
  public levelError;
  public questionError;
  public number = 0;

  // temp vars
  public topic;
  public level;
  public question;
  public numRightAnswers = 1;
  public numDistractors = 1;
  public distractor1;
  public distractor2;
  public distractor3;
  public rightAnswer1;
  public rightAnswer2;
  public rightAnswer3;
  public stimulus;


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
    validateRightAnswerText() {
      switch (this.numRightAnswers) {
        case 1: {
          this.rightAnswerTextError = this.utilsService.validateRightAnswerText(this.rightAnswer1);
          break;
        }
        case 2: {
          let res1 = this.utilsService.validateRightAnswerText(this.rightAnswer1);
          let res2 = this.utilsService.validateRightAnswerText(this.rightAnswer2);
          if (res1 === false || res2 === false) {
            this.rightAnswerTextError = false;
          }
          break;
        }
        case 2: {
          let res1 = this.utilsService.validateRightAnswerText(this.rightAnswer1);
          let res2 = this.utilsService.validateRightAnswerText(this.rightAnswer2);
          let res3 = this.utilsService.validateRightAnswerText(this.rightAnswer3);
          if (res1 === false || res2 === false || res3 === false) {
            this.rightAnswerTextError = false;
          }
          break;
        }
      }
  }


submit () {

  this.validateTopic();
  this.validateLevel();
  this.validateQuestion();
  this.validateRightAnswerText();

  if (this.topicError === false && this.levelError === false && this.questionError === false &&
    this.rightAnswerTextError === false) {
      this.registerMultipleChoiceTextExercise();
  }
}

  ngOnInit() {
    this.loading = true;
    this.resourcesService.fetchResources().subscribe(
      resResources => {
        this.stimulusImgs = this.resourcesService.getResourcesByType('stimuli');
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
    this.newExercise.type = 'text';
    this.newExercise.answers = [];
    this.newExercise.rightAnswers = [];
    this.newExercise.distractors = [];
  }

  registerMultipleChoiceTextExercise() {
    this.error = undefined;

    this.newExercise.topic = this.topic;
    this.newExercise.level = this.level;
    this.newExercise.question = this.question;

    const stimulus = this.stimulusImgs.filter((stimulus2) => { return stimulus2.selected; });
    if (stimulus.length === 1) {
      this.newExercise.stimulus = stimulus[0].resourceId;
    } else {
      this.newExercise.stimulus = null;
    }

    if (this.rightAnswer1 !== '' && this.rightAnswer1 !== undefined) {
      this.newExercise.rightAnswers.push(this.rightAnswer1);
    }
    if (this.rightAnswer2 !== '' && this.rightAnswer2 !== undefined) {
      this.newExercise.rightAnswers.push(this.rightAnswer2);
    }

    if (this.rightAnswer3 !== '' && this.rightAnswer3 !== undefined) {
      this.newExercise.rightAnswers.push(this.rightAnswer3);
    }

    if (this.distractor1 !== '' && this.distractor1 !== undefined) {
      this.newExercise.distractors.push(this.distractor1);
    }
    if (this.distractor2 !== '' && this.distractor2 !== undefined) {
      this.newExercise.distractors.push(this.distractor2);
    }
    if (this.distractor3 !== '' && this.distractor3 !== undefined) {
      this.newExercise.distractors.push(this.distractor3);
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
            //this.newExercise.answers = [];
            //this.newExercise.answersImg = [];
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
