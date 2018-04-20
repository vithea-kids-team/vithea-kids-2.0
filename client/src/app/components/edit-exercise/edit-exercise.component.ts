import { Component, OnInit, OnChanges } from '@angular/core';
import { ActivatedRoute, Params }   from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { Router } from '@angular/router';
import { Exercise } from '../../models/exercise';
import { Resource } from '../../models/resource';
import { ResourcesService } from '../../services/resources/resources.service';
import { ExercisesService } from '../../services/exercises/exercises.service';
import { Overlay } from 'ngx-modialog';
import { Modal } from 'ngx-modialog/plugins/bootstrap';
import { Location } from '@angular/common';
import { EditExerciseMultipleChoiceImageComponent } from '../../components/multiple-choice-exercise/edit-exercise-mc-image/edit-exercise-mc-image.component';
import { EditExerciseMultipleChoiceTextComponent } from '../../components/multiple-choice-exercise/edit-exercise-mc-text/edit-exercise-mc-text.component';
import { EditExerciseSpeechComponent } from '../../components/speech-exercise/edit-exercise-speech/edit-exercise-speech.component';
import { SimpleChanges } from '@angular/core/src/metadata/lifecycle_hooks';

@Component({
  selector: 'edit-exercise',
  templateUrl: './edit-exercise.component.html',
  styleUrls: ['./edit-exercise.component.css']
})
export class EditExerciseComponent implements OnInit {

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
    public exercisesService: ExercisesService, public router: Router, public location: Location) {}

  ngOnInit() {
    this.route.params
    .switchMap((params: Params) => Observable.of(params))
    .subscribe(params => {
      this.type = params['type'];
    });
  }

  goBack() {
    this.location.back();
  }

}
