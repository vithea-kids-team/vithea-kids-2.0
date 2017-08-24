import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params }   from '@angular/Router';
import { Observable } from 'rxjs/Observable';
import { Router } from '@angular/Router';

import { Exercise } from '../../models/exercise';
import { Resource } from '../../models/resource';
import { ResourcesService } from '../../services/resources/resources.service';
import { ExercisesService } from '../../services/exercises/exercises.service'

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

  constructor(public route: ActivatedRoute, public resourcesService: ResourcesService,
    public exercisesService: ExercisesService, public router: Router) {}

  ngOnInit() {
    this.loading = true;
    this.resourcesService.fetchResources().subscribe(
      res => {
        this.stimulusImgs = this.resourcesService.getResourcesByType('stimuli');
        this.rightAnswerImgs = this.resourcesService.getResourcesByType('stimuli');

        this.answersImgs1 = this.resourcesService.getResourcesByType('stimuli');
        this.answersImgs2 = this.resourcesService.getResourcesByType('stimuli');
        this.answersImgs3 = this.resourcesService.getResourcesByType('stimuli');
        this.loading = false;
      }
    )
    this.loading = true;
    this.resourcesService.fetchLevels().subscribe(
        res => {
            this.levels = res;
            this.loading = false;
        }
    )
    this.loading = true;
    this.resourcesService.fetchTopics().subscribe(
        res => {
            this.topics = res;
            this.loading = false;
        }
    )

    this.loading = true;

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
              console.log(res)
              this.newExercise.exerciseId = res.exerciseId;
              this.newExercise.exerciseName = res.name;
              this.newExercise.type = res.type.toLowerCase();
              this.newExercise.topic = res.topic.topicId;
              this.newExercise.level = res.level.levelId;
              this.newExercise.question = res.question.questionDescription;
              this.newExercise.stimulusText = res.question.stimulusText;
              this.newExercise.rightAnswer = res.rightAnswer.answerDescription;

              // remove the correct answer
              res.answers.reverse();
              if (res.answers.length === 2) {
                this.newExercise.distractor1 = res.answers[0].answerDescription;
              } else if (res.answers.length === 3) {
                this.newExercise.distractor1 = res.answers[1].answerDescription
                this.newExercise.distractor2 = res.answers[0].answerDescription
              } else if (res.answers.length === 4) {
                this.newExercise.distractor1 = res.answers[1].answerDescription
                this.newExercise.distractor2 = res.answers[0].answerDescription
                this.newExercise.distractor3 = res.answers[2].answerDescription
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

  registerExercise() {
    this.error = undefined;
    const stimulus = this.stimulusImgs.filter((stimulus) => { return stimulus.selected; });
    if (stimulus.length > 0) {
      this.newExercise.stimulus = stimulus[0].resourceId;
    }
    if (this.newExercise.type === 'image') {
      const rightAnswer = this.rightAnswerImgs.filter((rAnswer) => { return rAnswer.selected; });
      const answersImg = this.answersImgs.filter((stimulus) => { return stimulus.selected; });

      if (rightAnswer.length > 0) {
        this.newExercise.rightAnswerImg = rightAnswer[0].resourceId;
      }
      if (answersImg.length > 0) {
        answersImg.forEach(answer => {
          this.newExercise.answersImg.push(answer.resourceId);
        });
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
        this.exercisesService.editExercise(this.newExercise).subscribe(
            result => this.router.navigate(['/exercises/']),
            err => {
              this.error = err._body;
              console.error('Error editing an exercise.', err);
            }
        );
    } else {
      this.exercisesService.addExercise(this.newExercise).subscribe(
          result => {
            if (this.newExercise.sequenceId) {
              this.router.navigate(['/sequences/' + this.newExercise.sequenceId]);
            } else {
              this.router.navigate(['/exercises']);
            }
          },
          err => console.error('Error loading exercises.' + err),
          () => {
            this.newExercise.answers = [];
            this.newExercise.answersImg = [];
          }
        );
    }
}

updateType (type2: string) {
    this.type = type2;
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
