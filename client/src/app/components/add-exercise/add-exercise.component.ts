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

  private newExercise = new Exercise();
  private newAnswer : string = '';
  private stimulusImgs = [];
  private rightAnswerImgs = [];
  private answersImgs = [];


  constructor(private route: ActivatedRoute, private resourcesService: ResourcesService, private exercisesService : ExercisesService, private router: Router) {}

  ngOnInit() {

    this.resourcesService.fetchResources().subscribe(
      res => {
        this.stimulusImgs = this.resourcesService.getResourcesByType('stimuli');
        this.rightAnswerImgs = this.resourcesService.getResourcesByType('stimuli');
        this.answersImgs = this.resourcesService.getResourcesByType('stimuli');
      }
    )

    this.newExercise.type = 'text';
    this.newExercise.answers = [];
    this.newExercise.answersImg = [];

    this.route.params
      .switchMap((params: Params) => Observable.of(params))
      .subscribe(params => {
        const sequenceId : number = parseInt(params['sequenceid']);
        const exerciseId : number = parseInt(params['exerciseid']);
        if(sequenceId) {
          this.newExercise.sequenceId = sequenceId;
        } else if (exerciseId) {
          this.exercisesService.getExercise(exerciseId).subscribe(
            (res : any) => {
              console.log(res)
              this.newExercise.exerciseId = res.exerciseId;
              this.newExercise.type = res.type.toLowerCase();
              this.newExercise.topic = res.topic.topicId;
              this.newExercise.level = res.level.levelId;
              this.newExercise.question = res.question.questionDescription;
              this.newExercise.stimulusText = res.question.stimulusText;
              this.newExercise.rightAnswer = res.rightAnswer.answerDescription;

            },
            err => console.error("Error getting exercise", err)
          );
        }
      });
  }

  addAnswer() {
    if(this.newAnswer !== '') {
      this.newExercise.answers.push(this.newAnswer);
      this.newAnswer = '';
    }
  }

  removeAnswer(index : number) {
    this.newExercise.answers.splice(index, 1);
  }

  registerExercise() {
    const stimulus = this.stimulusImgs.filter((stimulus) => { return stimulus.selected;});
    if (stimulus.length > 0)
      this.newExercise.stimulus = stimulus[0].resourceId;
    if (this.newExercise.type === 'image') {
      const rightAnswer = this.rightAnswerImgs.filter((rAnswer) => { return rAnswer.selected;});
      const answersImg = this.answersImgs.filter((stimulus) => { return stimulus.selected;});

      if (rightAnswer.length > 0)
        this.newExercise.rightAnswerImg = rightAnswer[0].resourceId;

      if (answersImg.length > 0) {
        answersImg.forEach(answer => {
          this.newExercise.answersImg.push(answer.resourceId);
        });
      }
    }

    this.exercisesService.registerExercise(this.newExercise).subscribe(
      result => {
        if (this.newExercise.sequenceId) {
          this.router.navigate(['/sequences/'+ this.newExercise.sequenceId]);
        }
        else {
          this.router.navigate(['/exercises']);
        }
      }, 
      err => console.error("Error loading exercises. " + err),
      () => {
        this.newExercise.answers = [];
        this.newExercise.answersImg = [];
      }
    );
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
