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
  public newAnswer = '';
  public stimulusImgs = [];
  public rightAnswerImgs = [];
  public answersImgs = [];
  public topics = [];
  public levels = [];
  public error: string = undefined;

  constructor(public route: ActivatedRoute, public resourcesService: ResourcesService,
    public exercisesService: ExercisesService, public router: Router) {}

  ngOnInit() {

    this.resourcesService.fetchResources().subscribe(
      res => {
        this.stimulusImgs = this.resourcesService.getResourcesByType('stimuli');
        this.rightAnswerImgs = this.resourcesService.getResourcesByType('stimuli');
        this.answersImgs = this.resourcesService.getResourcesByType('stimuli');
      }
    )

    this.resourcesService.fetchLevels().subscribe(
        res => {
            this.levels = res;
        }
    )

    this.resourcesService.fetchTopics().subscribe(
        res => {
            this.topics = res;
        }
    )

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
        } else if (exerciseId) {
          this.exercisesService.getExercise(exerciseId).subscribe(
            (res: any) => {
              console.log(res)
              this.newExercise.exerciseId = res.exerciseId;
              this.newExercise.type = res.type.toLowerCase();
              this.newExercise.topic = res.topic.topicId;
              this.newExercise.level = res.level.levelId;
              this.newExercise.question = res.question.questionDescription;
              this.newExercise.stimulusText = res.question.stimulusText;
              this.newExercise.rightAnswer = res.rightAnswer.answerDescription;
            },
            err => console.error('Error getting exercise.', err)
          );
        }
      });
  }

  addAnswer() {
    if (this.newAnswer !== '') {
      this.newExercise.answers.push(this.newAnswer);
      this.newAnswer = '';
    }
  }

  removeAnswer(index: number) {
    this.newExercise.answers.splice(index, 1);
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

updateStimuli (results) {
    const last = results.length - 1;
    const lastItem = results[last];
    lastItem.selected = false;
    this.stimulusImgs.push(Object.assign({}, lastItem));
    this.rightAnswerImgs.push(Object.assign({}, lastItem));
    this.answersImgs.push(Object.assign({}, lastItem));
  }
}
