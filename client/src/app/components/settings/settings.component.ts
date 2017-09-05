import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';
import { ResourcesService } from '../../services/resources/resources.service';
import { ExercisesService} from '../../services/exercises/exercises.service';
import { Exercise } from '../../models/exercise';
import { Overlay } from 'ngx-modialog';
import { Modal } from 'ngx-modialog/plugins/bootstrap';

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
  private topicsInUse = [];
  private levelsInUse = [];
  public exercises: Array<Exercise>;
  private topicExists = false;
  private levelExists = false;
  success = false
  failure = false;
  textSuccess;
  textFailure;

  constructor(public resourcesService: ResourcesService, public exercisesService: ExercisesService,
    public location: Location, public modal: Modal) { }

  ngOnInit() {
    this.exercisesService.getExercises().subscribe(
      res => {
        this.exercises = res;
        this.initTopicsInUse();
        this.initLevelsInUse();

        this.resourcesService.fetchTopics().subscribe(
          resTopics => {
            this.topics = resTopics;
            this.initTempTopics();

            this.resourcesService.fetchLevels().subscribe(
              resLevels => {
                this.levels = resLevels;
                this.initTempLevels();
              }
            )
          }
        )
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

  initTopicsInUse() {
    this.exercises.forEach(element => {
      this.topicsInUse.push(element.topic);
    });
    let topicsInUse2 = this.topicsInUse;
    this.topicsInUse = [];
    topicsInUse2.forEach(element => {
      this.topicsInUse.push(element.topicDescription);
    });
  }

  initLevelsInUse() {
    this.exercises.forEach(element => {
      this.levelsInUse.push(element.level);
    });
    let levelsInUse2 = this.levelsInUse;
    this.levelsInUse = [];
    levelsInUse2.forEach(element => {
      this.levelsInUse.push(element.levelDescription);
    });
  }

  updateAddTopic() {
    this.topicExists = false;
    if (this.newTopic !== undefined ) {
      this.tempTopics.forEach(element => {
        if (element === this.newTopic && !this.topicExists) {
          this.failure = true;
          this.success = false;
          this.textFailure = 'Não foi possível adicionar o tema ' +  this.newTopic + '. Tema já existente.';
          console.error('The topic ' + this.newTopic + ' already exists.');
          this.newTopic = ' ';
          this.topicExists = true;
        }
      })
      if (!this.topicExists) {
        this.failure = false;
        this.success = true;
        this.textSuccess = 'Tema ' + this.newTopic + ' adicionado com sucesso.';
        this.tempTopics.push(this.newTopic);
        this.topicsToAdd.push(this.newTopic)
        this.newTopic = ' ';
      }
    } else {
      this.failure = true;
      this.success = false;
      this.textFailure = 'Não foi possível adicionar o tema por se encontrar vazio.';
      console.error('The topic should not be empty.')
    };
  }

  updateAddLevel() {
    this.levelExists = false;
      if (this.newLevel !== undefined ) {
        this.tempLevels.forEach(element => {
          if (element === this.newLevel && !this.levelExists) {
            this.failure = true;
            this.success = false;
            this.textFailure = 'Não foi possível adicionar o nível ' +  this.newLevel + '. Nível já existente.';
            console.error('The level ' + this.newLevel + ' already exists.');
            this.newLevel = ' ';
            this.levelExists = true;
          }
        })
        if (!this.levelExists) {
          this.failure = false;
          this.success = true;
          this.textSuccess = 'Nível ' + this.newLevel + ' adicionado com sucesso.';
          this.tempLevels.push(this.newLevel);
          this.levelsToAdd.push(this.newLevel)
          this.newLevel = ' ';
        }
      } else {
        this.failure = true;
        this.success = false;
        this.textFailure = 'Não foi possível adicionar o nível por se encontrar vazio.';
        console.error('The level should not be empty.')
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

  updateRemoveTopic(topic: string) {

    let indexInUse: number = this.topicsInUse.indexOf(topic);

    if (indexInUse !== -1) {
      this.failure = true;
      this.success = false;
      this.textFailure = 'Não foi possível remover o tema ' + topic + ' por se encontrar associado a pelo menos um exercício.';
    } else {
        let index: number = this.tempTopics.indexOf(topic);

        if (index !== -1) {
          this.tempTopics.splice(index, 1);
          this.topicsToRemove.push(topic);
        }
        let index2: number = this.topicsToAdd.indexOf(topic);
        if (index2 !== -1) {
          this.topicsToAdd.splice(index2, 1);
        }
        this.failure = false;
        this.success = true;
        this.textSuccess = 'Tema ' + topic + ' removido com sucesso.';
    }
  }

  updateRemoveLevel(level: string) {

    let indexInUse: number = this.levelsInUse.indexOf(level);

    if (indexInUse !== -1) {
      this.failure = true;
      this.success = false;
      this.textFailure = 'Não foi possível remover o nível ' + level + ' por se encontrar associado a pelo menos um exercício.';
    } else {

      let index: number = this.tempLevels.indexOf(level);
      if (index !== -1) {
          this.tempLevels.splice(index, 1);
          this.levelsToRemove.push(level);
      }
      let index2: number = this.levelsToAdd.indexOf(level);
      if (index2 !== -1) {
          this.levelsToAdd.splice(index2, 1);
      }
      this.failure = false;
      this.success = true;
      this.textSuccess = 'Nível ' + level + ' removido com sucesso.';
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
    const dialogRef = this.modal.confirm().size('lg').isBlocking(true).showClose(false).okBtn('Sim').cancelBtn('Não')
    .title('Editar definições').body(`Tem a certeza que pretende guardar as alterações efectuadas?`).open();

    dialogRef.then(dialogRef => { dialogRef.result.then(result => {
      if (result) {

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

        this.goBack();
      } else {
        this.goBack();
      }
    }).catch(() => {})});
  }

  goBack() {
    this.location.back();
  }

  reset() {
    this.success = false;
    this.failure = false;
    this.textFailure = '';
    this.textSuccess = '';
  }

}
