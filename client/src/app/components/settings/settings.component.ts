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
  private tempTopics = [];
  private tempLevels = [];
  private topicsToAdd = []
  private levelsToAdd = []
  private topicsToRemove = []
  private levelsToRemove = []
  private topicExists = false;
  private levelExists = false;

  constructor(public resourcesService: ResourcesService, public location: Location) { }

  ngOnInit() {
    this.resourcesService.fetchTopics().subscribe(
      res => {
        this.topics = res;
        this.initTempTopics();
        }
      )
    this.resourcesService.fetchLevels().subscribe(
      res => {
        this.levels = res;
        this.initTempLevels();
      }
    )
  }

  initTempTopics() {
    this.topics.forEach(element => {
      this.tempTopics.push(element.topicDescription);
    });
  }

  initTempLevels() {
    this.levels.forEach(element => {
      this.tempLevels.push(element.levelDescription);
    });
  }

  updateAddTopic() {
    this.topicExists = false;
    if (this.newTopic !== undefined ) {
      this.tempTopics.forEach(element => {
        if (element === this.newTopic && !this.topicExists) {
          console.log('The topic ' + this.newTopic + ' already exists.');
          this.newTopic = ' ';
          this.topicExists = true;
        }
      })
      if (!this.topicExists) {
        this.tempTopics.push(this.newTopic);
        this.topicsToAdd.push(this.newTopic)
        this.newTopic = ' ';
      }
    }else {
      console.log('The topic should not be empty.')
    };
  }

  updateAddLevel() {
    this.levelExists = false;
      if (this.newLevel !== undefined ) {
        this.tempLevels.forEach(element => {
          if (element === this.newLevel && !this.levelExists) {
            console.log('The level ' + this.newLevel + ' already exists.');
            this.newLevel = ' ';
            this.levelExists = true;
          }
        })
        if (!this.levelExists) {
          this.tempLevels.push(this.newLevel);
          this.levelsToAdd.push(this.newLevel)
          this.newLevel = ' ';
        }
      }else {
      console.log('The level should not be empty.')
    };
  }

  addTopic(topic: string) {
    if (topic && topic !== '') {
      this.loading = true;
        this.resourcesService.addTopic(topic).subscribe(
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

  addLevel(level: string) {
    if (level && level !== '') {
      this.loading = true;
        this.resourcesService.addLevel(level).subscribe(
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

  updateRemoveTopic(topic: string) {
    let index: number = this.tempTopics.indexOf(topic);
    if (index !== -1) {
        this.tempTopics.splice(index, 1);
        this.topicsToRemove.push(topic);
    }
    let index2: number = this.topicsToAdd.indexOf(topic);
    if (index2 !== -1) {
        this.topicsToAdd.splice(index2, 1);
    }
  }

  updateRemoveLevel(level: string) {
    let index: number = this.tempLevels.indexOf(level);
    if (index !== -1) {
        this.tempLevels.splice(index, 1);
        this.levelsToRemove.push(level);
    }
    let index2: number = this.levelsToAdd.indexOf(level);
    if (index2 !== -1) {
        this.levelsToAdd.splice(index2, 1);
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
          )
        },
        err => {
          console.error('Error removing topic', err);
          this.loading = false;
        }
    )
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


  saveChanges() {

    // remove topics
    this.topicsToRemove.forEach(elemTemp => {
      this.topics.forEach(elemInit => {
        if (elemTemp === elemInit.topicDescription) {
          this.removeTopic(Number(elemInit.topicId))
        }
      });
    })

    // remove levels
    this.levelsToRemove.forEach(elemTemp => {
      this.levels.forEach(elemInit => {
        if (elemTemp === elemInit.levelDescription) {
          this.removeLevel(Number(elemInit.levelId))
        }
      });
    })

    // add all topics
    this.topicsToAdd.forEach(element => {
      this.addTopic(element);
    });
    console.log(this.topics.length);

    // add all levels
    this.levelsToAdd.forEach(element => {
      this.addLevel(element);
    });

    this.tempLevels = [];
    this.tempTopics = [];
    this.topicsToAdd = [];
    this.topicsToRemove = [];
    this.levelsToAdd = [];
    this.levelsToRemove = [];

    this.location.back();
  }

  cancel() {
    this.location.back();
  }

}
