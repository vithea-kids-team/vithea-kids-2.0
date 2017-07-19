import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';

import { ResourcesService } from '../../services/resources/resources.service';

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.css']
})
export class SettingsComponent implements OnInit {

  public topics = [];
  public levels = [];
  public newTopic: string;
  public newLevel: string;
  public loading = false;

  constructor(public resourcesService: ResourcesService, public location: Location) { }

  ngOnInit() {
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
    }

  addTopic() {
    if (this.newTopic && this.newTopic !== '') {
      this.loading = true;
      this.resourcesService.addTopic(this.newTopic).subscribe(
        res => {
          this.newTopic = '';
          this.resourcesService.fetchTopics().subscribe(
            res => {
              this.topics = res;
              this.loading = false
            }
          )
        },
        err => {
          console.error('Error adding topic', err);
          this.loading = false;
        }
      )
    }
  }

  removeTopic(topic: number) {
    this.loading = true;
    this.resourcesService.removeTopic(topic).subscribe(
        res => {
          this.resourcesService.fetchTopics().subscribe(
          res => {
              this.topics = res;
              this.loading = false
            }
          );
        },
        err => {
          console.error('Error removing topic', err);
          this.loading = false;
        }
      )
  }

  addLevel() {
    if (this.newLevel && this.newLevel !== '') {
      this.loading = true;
      this.resourcesService.addLevel(this.newLevel).subscribe(
        res => {
          this.newLevel = '';
          this.resourcesService.fetchLevels().subscribe(
          res => {
              this.levels = res;
              this.loading = false
            }
          )
        },
        err => {
          console.error('Error adding level', err);
          this.loading = false;
        }
      )
    }
  }

  removeLevel(level: number) {
    this.loading = true;
    this.resourcesService.removeLevel(level).subscribe(
        res => {
          this.resourcesService.fetchLevels().subscribe(
          res => {
              this.levels = res;
              this.loading = false
            }
          )
        },
        err => {
          console.error('Error removing level', err);
          this.loading = false;
        }
      )
  }

  goBack() {
    this.location.back();
  }

}
