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

  public newExercise = new Exercise();
  public question = '';
  public newAnswer = '';
  public stimulusImgs = [];
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
  validateRightAnswerText() {
    this.rightAnswerTextError = this.utilsService.validateRightAnswerText(this.newExercise.rightAnswer);
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
    this.loading = false;

    this.newExercise.type = 'text';
    this.newExercise.answers = [];

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

            this.newExercise.stimulus = res.question.stimulus;
            this.newExercise.rightAnswer = res.rightAnswer.answerDescription;
            res.answers.reverse();
            if (res.answers.length === 2) {
              this.newExercise.distractor1 = res.answers[0].answerDescription;
            } else if (res.answers.length === 3) {
              this.newExercise.distractor1 = res.answers[1].answerDescription;
              this.newExercise.distractor2 = res.answers[0].answerDescription;
            } else if (res.answers.length === 4) {
               this.newExercise.distractor1 = res.answers[1].answerDescription;
               this.newExercise.distractor2 = res.answers[0].answerDescription;
               this.newExercise.distractor3 = res.answers[2].answerDescription;
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

editMultipleChoiceTextExercise() {
    this.error = undefined;

    const stimulus = this.stimulusImgs.filter((stimulus2) => { return stimulus2.selected; });
    if (stimulus.length === 1) {
      this.newExercise.stimulus = stimulus[0].resourceId;
    } else {
      this.newExercise.stimulus = null;
    }

    if (this.newExercise.distractor1 !== '' && this.newExercise.distractor1 !== undefined) {
      this.newExercise.answers.push(this.newExercise.distractor1);
    }
    if (this.newExercise.distractor2 !== '' && this.newExercise.distractor2 !== undefined) {
      this.newExercise.answers.push(this.newExercise.distractor2);
    }
    if (this.newExercise.distractor3 !== '' && this.newExercise.distractor3 !== undefined) {
      this.newExercise.answers.push(this.newExercise.distractor3);
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
