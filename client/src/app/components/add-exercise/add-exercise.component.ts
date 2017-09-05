import { Component, OnInit, OnChanges } from '@angular/core';
import { ActivatedRoute, Params }   from '@angular/Router';
import { Observable } from 'rxjs/Observable';
import { Router } from '@angular/Router';
import { Exercise } from '../../models/exercise';
import { Resource } from '../../models/resource';
import { ResourcesService } from '../../services/resources/resources.service';
import { ExercisesService } from '../../services/exercises/exercises.service';
import { Overlay } from 'ngx-modialog';
import { Modal } from 'ngx-modialog/plugins/bootstrap';
import { Location } from '@angular/common';

@Component({
  selector: 'app-add-exercise',
  templateUrl: './add-exercise.component.html',
  styleUrls: ['./add-exercise.component.css']
})
export class AddExerciseComponent implements OnInit {

  public newExercise = new Exercise();
  public question = '';
  public newAnswer = '';
  public stimulusImgs = [];
  public rightAnswerImgs = [];
  public answersImgs = [];
  public answersImgs1 = [];
  public answersImgs2 = [];
  public answersImgs3 = [];
  public topics = [];
  public levels = [];
  public error: string = undefined;
  public loading = false;
  public type = '';

  constructor(public modal: Modal, public route: ActivatedRoute, public resourcesService: ResourcesService,
    public exercisesService: ExercisesService, public router: Router, public location: Location) {}

  ngOnInit() {
    this.loading = true;
    this.resourcesService.fetchResources().subscribe(
      resResources => {
        this.stimulusImgs = this.resourcesService.getResourcesByType('stimuli');
        this.rightAnswerImgs = this.resourcesService.getResourcesByType('stimuli');
        this.answersImgs1 = this.resourcesService.getResourcesByType('stimuli');
        this.answersImgs2 = this.resourcesService.getResourcesByType('stimuli');
        this.answersImgs3 = this.resourcesService.getResourcesByType('stimuli');
        this.loading = false;
        this.resourcesService.fetchLevels().subscribe(
          resLevels => {
              this.levels = resLevels;
              this.loading = false;
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

    if (this.exercisesService.edited === true) {
      this.newExercise.exerciseId = this.exercisesService.exerciseId;
      this.newExercise.exerciseName = this.exercisesService.exerciseName;
      this.newExercise.type = this.exercisesService.exerciseType;
      this.newExercise.question = this.exercisesService.exerciseQuestion;
      this.newExercise.topic = this.exercisesService.exerciseTopic;
      this.newExercise.level = this.exercisesService.exerciseLevel;
      this.newExercise.question = this.exercisesService.exerciseQuestion;

      if (this.exercisesService.exerciseType === 'text') {
        this.newExercise.rightAnswer = this.exercisesService.exerciseRightAnswer;
        this.newExercise.stimulus = this.exercisesService.exerciseStimulus;
        this.newExercise.distractor1 = this.exercisesService.exerciseDistractor1;
        this.newExercise.distractor2 = this.exercisesService.exerciseDistractor2;
        this.newExercise.distractor3 = this.exercisesService.exerciseDistractor3;
      } else {
        this.newExercise.rightAnswer = this.exercisesService.exerciseRightAnswer;
        this.newExercise.stimulusText = this.exercisesService.exerciseStimulusText;
        this.newExercise.distractor1 = this.exercisesService.exerciseDistractor1;
        this.newExercise.distractor2 = this.exercisesService.exerciseDistractor2;
        this.newExercise.distractor3 = this.exercisesService.exerciseDistractor3;

      }
      this.loading = false;
      this.exercisesService.edited = false;
    } else {
      this.newExercise.type = 'text';
      this.newExercise.answers = [];
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

                // text stuff
                if (this.newExercise.type === 'text') {
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
                }
                // image stuff
                if (this.newExercise.type === 'image') {
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
  }


  goToPreferences() {
    this.exercisesService.edited = true;
    this.exercisesService.exerciseId = this.newExercise.exerciseId;
    this.exercisesService.exerciseName = this.newExercise.exerciseName;
    this.exercisesService.exerciseType = this.newExercise.type;
    this.exercisesService.exerciseTopic = this.newExercise.topic;
    this.exercisesService.exerciseLevel = this.newExercise.level;
    this.exercisesService.exerciseQuestion = this.newExercise.question;

    if (this.exercisesService.exerciseType === 'text') {
      this.exercisesService.exerciseRightAnswer = this.newExercise.rightAnswer;
      const stimulus = this.stimulusImgs.filter((stimulus) => { return stimulus.selected; });
      if (stimulus.length > 0) {
        this.exercisesService.exerciseStimulus = stimulus[0];
      }
      this.exercisesService.exerciseDistractor1 = this.newExercise.distractor1;
      this.exercisesService.exerciseDistractor2 = this.newExercise.distractor2;
      this.exercisesService.exerciseDistractor3 = this.newExercise.distractor3;
    } else {
      this.exercisesService.exerciseStimulusText = this.newExercise.stimulusText;
      const rightAnswer = this.rightAnswerImgs.filter((rAnswer) => { return rAnswer.selected; });
      const answersImg1 = this.answersImgs1.filter((stimulus) => { return stimulus.selected; });
      const answersImg2 = this.answersImgs2.filter((stimulus) => { return stimulus.selected; });
      const answersImg3 = this.answersImgs3.filter((stimulus) => { return stimulus.selected; });

      if (rightAnswer.length === 1) {
        this.exercisesService.exerciseRightAnswer = rightAnswer[0];
      }
      if (answersImg1.length === 1) {
        this.exercisesService.exerciseDistractor1 = answersImg1[0];
      }
      if (answersImg2.length === 1) {
        this.exercisesService.exerciseDistractor2 = answersImg2[0];
      }
      if (answersImg3.length === 1) {
        this.exercisesService.exerciseDistractor3 = answersImg3[0];
      }
    }

    this.router.navigate(['/settings']);
  }

  registerExercise() {
    this.error = undefined;
    const stimulus = this.stimulusImgs.filter((stimulus) => { return stimulus.selected; });
    if (stimulus.length > 0) {
      this.newExercise.stimulus = stimulus[0].resourceId;
    }
    if (this.newExercise.type === 'image') {
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
    }

    if (this.newExercise.distractor1 !== '') {
      this.newExercise.answers.push(this.newExercise.distractor1);
    }
    if (this.newExercise.distractor2 !== '') {
      this.newExercise.answers.push(this.newExercise.distractor2);
    }
    if (this.newExercise.distractor3 !== '') {
      this.newExercise.answers.push(this.newExercise.distractor3);
    }

    if (this.newExercise.exerciseId) {
      const dialogRef = this.modal.confirm().size('lg').isBlocking(true).showClose(false).okBtn('Sim').cancelBtn('Não')
      .title('Editar exercício').body(`Tem a certeza que pretende editar o exercício?`).open();

      dialogRef.then(dialogRef => { dialogRef.result.then(result => {
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
  } else {
    const dialogRef = this.modal.confirm().size('lg').isBlocking(true).showClose(false).okBtn('Sim').cancelBtn('Não')
    .title('Registar exercício').body('Tem a certeza que pretende registar o exercício?').open();

    dialogRef.then(dialogRef => { dialogRef.result.then(result => {
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
            this.newExercise.answersImg = [];
          }
        );
      } else {
        this.goBack();
      }
    }).catch(() => {})});
  }
}

updateType (type2: string) {
    this.type = type2;
}

goBack() {
  this.location.back();
}

updateStimuli (results) {
    this.loading = true;
    const last = results.length - 1;
    const lastItem = results[last];
    lastItem.selected = false;
    this.stimulusImgs.push(Object.assign({}, lastItem));
    this.rightAnswerImgs.push(Object.assign({}, lastItem));
    this.answersImgs.push(Object.assign({}, lastItem));
    this.loading = false;
  }
}
