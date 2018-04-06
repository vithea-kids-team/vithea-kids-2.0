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
  selector: 'app-add-exercise-speech',
  templateUrl: './add-exercise-speech.component.html',
  styleUrls: ['./add-exercise-speech.component.css']
})
export class AddExerciseSpeechComponent implements OnInit {

  public newExercise = new Exercise();
  public newAnswer = '';
  public stimulusImgs = [];
  public topics = [];
  public levels = [];
  public error: string = undefined;
  public loading = false;
  public rightAnswerTextError;
  public topicError;
  public levelError;
  public questionError;
  public number = 0;

 // temp vars
 public rightAnswer1;
 public rightAnswer2;
 public rightAnswer3;
 public rightAnswer4;
 public rightAnswer5;
 public rightAnswer6;
 public exerciseId;
 public exerciseName;
 public topic;
 public level;
 public question;
 public numRightAnswers = 1;
 public stimulus;
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
  validateRightAnswerText() {

    let res1 = this.utilsService.validateRightAnswerText(this.rightAnswer1);
    let res2 = this.utilsService.validateRightAnswerText(this.rightAnswer2);
    let res3 = this.utilsService.validateRightAnswerText(this.rightAnswer3);
    let res4 = this.utilsService.validateRightAnswerText(this.rightAnswer4);
    let res5 = this.utilsService.validateRightAnswerText(this.rightAnswer5);
    let res6 = this.utilsService.validateRightAnswerText(this.rightAnswer6);

    switch (this.numRightAnswers) {
      case 1: {
        this.rightAnswerTextError = res1;
        break;
      }
      case 2: {
        if (res1 === true || res2 === true) {
          this.rightAnswerTextError = true;
        }
        if (res1 === false || res2 === false) {
          this.rightAnswerTextError = false;
        }
        break;
      }
      case 3: {
        if (res1 === true || res2 === true || res3 === true) {
          this.rightAnswerTextError = true;
        }
        if (res1 === false || res2 === false || res3 === false) {
          this.rightAnswerTextError = false;
        }
        break;
      }
      case 4: {
        if (res1 === true || res2 === true || res3 === true || res4 === true) {
          this.rightAnswerTextError = true;
        }
        if (res1 === false || res2 === false || res3 === false || res4 === true) {
          this.rightAnswerTextError = false;
        }
        break;
      }
      case 5: {
        if (res1 === true || res2 === true || res3 === true || res4 === true || res5 === true) {
          this.rightAnswerTextError = true;
        }
        if (res1 === false || res2 === false || res3 === false || res4 === true || res5 === false) {
          this.rightAnswerTextError = false;
        }
        break;
      }
      case 6: {
        if (res1 === true || res2 === true || res3 === true || res4 === true || res5 === true || res6 === true) {
          this.rightAnswerTextError = true;
        }
        if (res1 === false || res2 === false || res3 === false || res4 === true || res5 === false || res6 === false) {
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
        this.registerSpeechExercise();
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
      this.newExercise.type = 'speech';
      this.newExercise.answers = [];
      this.newExercise.rightAnswers = [];
    }

    registerSpeechExercise() {
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
      if (this.rightAnswer4 !== '' && this.rightAnswer4 !== undefined) {
        this.newExercise.rightAnswers.push(this.rightAnswer4);
      }
      if (this.rightAnswer5 !== '' && this.rightAnswer5 !== undefined) {
        this.newExercise.rightAnswers.push(this.rightAnswer5);
      }
      if (this.rightAnswer6 !== '' && this.rightAnswer6 !== undefined) {
        this.newExercise.rightAnswers.push(this.rightAnswer6);
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
    goBack() {
      this.location.back();
    }

}
