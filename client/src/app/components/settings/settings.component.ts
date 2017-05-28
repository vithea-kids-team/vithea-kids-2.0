import { Component } from '@angular/core';
import {Location} from '@angular/common';

import { ResourcesService } from '../../services/resources/resources.service';

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.css']
})
export class SettingsComponent {

  newTopic : string;
  newLevel : string;

  constructor(private resourcesService : ResourcesService, private location : Location) { }

  addTopic() {
    if(this.newTopic !== '') {
      this.resourcesService.addTopic(this.newTopic).subscribe(
        res => {
          this.newTopic = '';
          this.resourcesService.fetchTopics();
        },
        err => console.error("Error adding topic", err)
      )
    }
  }

  removeTopic(topic : number) {
    this.resourcesService.removeTopic(topic).subscribe(
        res => {
          this.resourcesService.fetchTopics();
        },
        err => console.error("Error removing topic", err)
      )
  }

  addLevel() {
    if(this.newLevel !== '') {
      this.resourcesService.addLevel(this.newLevel).subscribe(
        res => {
          this.newLevel = '';
          this.resourcesService.fetchLevels();
        },
        err => console.error("Error adding level", err)
      )
    }
  }

  removeLevel(level : number) {
     this.resourcesService.removeLevel(level).subscribe(
        res => {
          this.resourcesService.fetchLevels();
        },
        err => console.error("Error removing level", err)
      )
  }

  goBack() {
    this.location.back();
  }

}
