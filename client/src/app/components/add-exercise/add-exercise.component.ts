import { Component, OnInit } from '@angular/core';
import { Exercise } from '../../models/exercise';
import { ResourcesService } from '../../services/resources/resources.service';

@Component({
  selector: 'app-add-exercise',
  templateUrl: './add-exercise.component.html',
  styleUrls: ['./add-exercise.component.css']
})
export class AddExerciseComponent implements OnInit {

  public newexercise = new Exercise();

  constructor(private resourcesService: ResourcesService) { }

  ngOnInit() {

  }

}
