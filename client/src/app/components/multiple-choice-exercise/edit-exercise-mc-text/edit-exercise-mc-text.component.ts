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
  selector: 'app-edit-exercise-mc-text',
  templateUrl: './edit-exercise-mc-text.component.html',
  styleUrls: ['./edit-exercise-mc-text.component.css']
})
export class EditExerciseMultipleChoiceTextComponent implements OnInit {

  public editExercise = new Exercise();
  public newAnswer = '';
  public stimulusImgs = [];
  public topics = [];
  public levels = [];
  public type = [];
  public error: string = undefined;
  public loading = false;
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
  public numRightAnswers;
  public numDistractors;
  public distractor1;
  public distractor2;
  public distractor3;
  public rightAnswer1;
  public rightAnswer2;
  public rightAnswer3;
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
  validateRightAnswerText() {
    switch (this.numRightAnswers) {
      case 1: {
        this.rightAnswerTextError = this.utilsService.validateRightAnswerText(this.rightAnswer1);
        break;
      }
      case 2: {
        let res1 = this.utilsService.validateRightAnswerText(this.rightAnswer1);
        let res2 = this.utilsService.validateRightAnswerText(this.rightAnswer2);
        if (res1 === true || res2 === true) {
          this.rightAnswerTextError = true;
        }
        if (res1 === false || res2 === false) {
          this.rightAnswerTextError = false;
        }
        break;
      }
      case 3: {
        let res1 = this.utilsService.validateRightAnswerText(this.rightAnswer1);
        let res2 = this.utilsService.validateRightAnswerText(this.rightAnswer2);
        let res3 = this.utilsService.validateRightAnswerText(this.rightAnswer3);
        if (res1 === true || res2 === true || res3 === true) {
          this.rightAnswerTextError = true;
        }
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
      this.editMultipleChoiceTextExercise();
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
                }
            )
          }
        )
      }
    )
    this.editExercise.type = 'text';
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
            this.exerciseId = res.exerciseId;
            this.exerciseName = res.name;
            this.type = res.type.toLowerCase();
            this.topic = res.topic.topicId;
            this.level = res.level.levelId;
            this.question = res.question.questionDescription;
            this.stimulus = res.question.stimulus;

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
                this.rightAnswer1 = this.rightAnswers[0].answerDescription;
                break;
              } case 2: {
                this.rightAnswer1 = this.rightAnswers[0].answerDescription;
                this.rightAnswer2 = this.rightAnswers[1].answerDescription;
                break;
              } case 3: {
                this.rightAnswer1 = this.rightAnswers[0].answerDescription;
                this.rightAnswer2 = this.rightAnswers[1].answerDescription;
                this.rightAnswer3 = this.rightAnswers[2].answerDescription;
                break;
              }
            }
            
            this.numDistractors = this.distractors.length;
            switch (this.numDistractors) {
              case 1: {
                this.distractor1 = this.distractors[0].answerDescription;
                break;
              } case 2: {
                this.distractor1 = this.distractors[0].answerDescription;
                this.distractor2 = this.distractors[1].answerDescription;
                break;
              } case 3: {
                this.distractor1 = this.distractors[0].answerDescription;
                this.distractor2 = this.distractors[1].answerDescription;
                this.distractor3 = this.distractors[2].answerDescription;
                break;
              }
            }
            this.loading = false;
          },
          err => {
            console.error('Error getting exercise.', err);
            this.loading = false;
        }
        );
      }
    });
}

editMultipleChoiceTextExercise() {
    this.error = undefined;

    this.editExercise.level = this.level;
    this.editExercise.topic = this.topic;
    this.editExercise.question = this.question;
    this.editExercise.exerciseId = this.exerciseId;
    this.editExercise.exerciseName = this.exerciseName;
    const stimulus = this.stimulusImgs.filter((stimulus2) => { return stimulus2.selected; });
    if (stimulus.length === 1) {
      this.editExercise.stimulus = stimulus[0].resourceId;
    } else {
      this.editExercise.stimulus = null;
    }

    if (this.rightAnswer1 !== '' && this.rightAnswer1 !== undefined) {
      this.editExercise.rightAnswers.push(this.rightAnswer1);
    }
    if (this.rightAnswer2 !== '' && this.rightAnswer2 !== undefined) {
      this.editExercise.rightAnswers.push(this.rightAnswer2);
    }
    if (this.rightAnswer3 !== '' && this.rightAnswer3 !== undefined) {
      this.editExercise.rightAnswers.push(this.rightAnswer3);
    }

    if (this.distractor1 !== '' && this.distractor1 !== undefined) {
      this.editExercise.distractors.push(this.distractor1);
    }
    if (this.distractor2 !== '' && this.distractor2 !== undefined) {
      this.editExercise.distractors.push(this.distractor2);
    }
    if (this.distractor3 !== '' && this.distractor3 !== undefined) {
      this.editExercise.distractors.push(this.distractor3);
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
