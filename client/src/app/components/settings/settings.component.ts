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

  success = false
  failure = false;
  textSuccess;
  textFailure;
  private topicExists = false;
  private levelExists = false;
  private topicsInUseId = [];
  private levelsInUseId = [];
  private topicsInUseName = [];
  private levelsInUseName = [];
  private tempTopicsName = [];
  private tempLevelsName = [];
  private tempTopicsId = [];
  private tempLevelsId = [];
  public topics = [];
  public levels = [];
  public exercises: Array<Exercise>;
  public newTopic: string;
  public newLevel: string;
  public loading = false;


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
              this.resourcesService.fetchLevels().subscribe(
                resLevels => {
                  this.levels = resLevels;
                  this.tempTopics();
                  this.tempTopics();
                }
              )
            }
          )
        }
      )
  }

  // topics and levels removed during settings
  // topics and levels added during settings
  // topics and levels in use
  initTopicsInUse() {
    let topicsInUse = [];
    this.exercises.forEach(element => {
      topicsInUse.push(element.topic);
    });
    topicsInUse.forEach(element => {
      this.topicsInUseId.push(element.topicId);
      this.topicsInUseName.push(element.topicDescription);
    });
  }

  initLevelsInUse() {
    let levelsInUse = [];
    this.exercises.forEach(element => {
      levelsInUse.push(element.level);
    });
    levelsInUse.forEach(element => {
      this.levelsInUseId.push(element.levelId);
      this.levelsInUseName.push(element.levelDescription);
    });
  }

  tempTopics() {
    this.tempTopicsName = [];
    this.tempTopicsId = [];
    this.topics.forEach(element => {
      this.tempTopicsName.push(element.topicDescription);
      this.tempTopicsId.push(element.topicId);
    });
  }

  tempLevels() {
    this.tempLevelsName = [];
    this.tempLevelsId = [];
    this.levels.forEach(element => {
      this.tempLevelsName.push(element.levelDescription);
      this.tempLevelsId.push(element.levelId);
    });
  }

  addTopic() {
    this.topicExists = false;

    if (this.newTopic === undefined ) {
      this.failure = true;
      this.success = false;
      this.textFailure = 'Não foi possível adicionar o tema por se encontrar vazio.';
      console.error('The topic should not be empty.')
    } else {
      const dialogRef = this.modal.confirm().size('lg').isBlocking(true).showClose(false).okBtn('Sim').cancelBtn('Não')
      .title('Editar definições').body('Tem a certeza que pretende adicionar o tema ' + this.newTopic + '?').open();
      dialogRef.then(dialogRef => { dialogRef.result.then(result => {
        if (result) {
            this.tempTopicsName.forEach(element => {
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
              this.loading = true;
              let topicName = this.newTopic;
              this.resourcesService.addTopic(this.newTopic).subscribe(
                resAdd => {
                  this.failure = false;
                  this.success = true;
                  this.textSuccess = 'Tema ' + topicName + ' adicionado com sucesso.';
                  this.topics = [];
                  this.resourcesService.fetchTopics().subscribe(
                    resTopics => {
                      this.topics = resTopics;
                      this.tempTopics();
                      this.newTopic = '';
                      this.loading = false
                    }
                  )
                },
                err => {
                  console.error('Error adding topic', err);
                  this.failure = true;
                  this.success = false;
                  this.textFailure = 'Não foi possível adicionar o tema.';
                  console.error('Error adding  topic.')
                  this.loading = false;
                }
              )
              this.newTopic = ' ';
            }
        } else {
        }
      }).catch(() => {})});
    }
  }

  addLevel() {
    this.levelExists = false;

    if (this.newLevel === undefined ) {
      this.failure = true;
      this.success = false;
      this.textFailure = 'Não foi possível adicionar o nível por se encontrar vazio.';
      console.error('The level should not be empty.')
    } else {
      const dialogRef = this.modal.confirm().size('lg').isBlocking(true).showClose(false).okBtn('Sim').cancelBtn('Não')
      .title('Editar definições').body('Tem a certeza que pretende adicionar o nível ' + this.newLevel + '?').open();
      dialogRef.then(dialogRef => { dialogRef.result.then(result => {
        if (result) {
            this.tempLevelsName.forEach(element => {
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
              this.loading = true;
              let levelName = this.newLevel;
              this.resourcesService.addLevel(this.newLevel).subscribe(
                resAdd => {
                  this.failure = false;
                  this.success = true;
                  this.textSuccess = 'Nível ' + levelName + ' adicionado com sucesso.';
                  this.levels = [];
                  this.resourcesService.fetchLevels().subscribe(
                    resLevels => {
                      this.levels = resLevels;
                      this.tempLevels();
                      this.newLevel = '';
                      this.loading = false
                    }
                  )
                },
                err => {
                  console.error('Error adding level', err);
                  this.failure = true;
                  this.success = false;
                  this.textFailure = 'Não foi possível adicionar o nível.';
                  console.error('Error adding  level.')
                  this.loading = false;
                }
              )
              this.newLevel = ' ';
            }
        } else {
        }
      }).catch(() => {})});
    }
  }

  removeTopic(topic: number) {

      let indexExist: number =  this.tempTopicsId.indexOf(topic);
      if (indexExist !== -1) {
        let topicDescription = this.tempTopicsName.slice(indexExist, indexExist + 1);

        const dialogRef = this.modal.confirm().size('lg').isBlocking(true).showClose(false).okBtn('Sim').cancelBtn('Não')
        .title('Editar definições').body('Tem a certeza que pretende remover o tema ' + topicDescription + '?').open();
        dialogRef.then(dialogRef => { dialogRef.result.then(result => {
          if (result) {
            let indexInUse: number = this.topicsInUseId.indexOf(topic);
            if (indexInUse !== -1) {
              this.failure = true;
              this.success = false;
              this.textFailure = 'Não foi possível remover o tema ' + this.topicsInUseName.slice(indexInUse, indexInUse + 1) + ' por se encontrar associado a pelo menos um exercício.';
            } else {
              this.loading = true;
              this.resourcesService.removeTopic(topic).subscribe(
                  resRemove => {
                    this.failure = false;
                    this.success = true;
                    this.textSuccess = 'Tema ' + topicDescription + ' removido com sucesso.';
                    console.log('Topic removed. ');
                    this.resourcesService.fetchTopics().subscribe(
                    resTopics => {
                       this.topics = resTopics;
                       this.tempTopics();
                       this.loading = false;
                     }
                    );
                  },
                  err => {
                    this.failure = true;
                    this.success = false;
                    this.textFailure = 'Não foi possível remover o tema ' + topicDescription + '.';
                    console.error('Error removing topic', err);
                    this.loading = false;
                  }
                )
            }
          } else {
          }
        }).catch(() => {})});
      } else {
        this.failure = true;
        this.success = false;
        this.textFailure = 'Não foi possível remover o tema.';
        console.error('Error removing topic');
        this.loading = false;
      }
  }

  removeLevel(level: number) {
          let indexExist: number =  this.tempLevelsId.indexOf(level);
          if (indexExist !== -1) {
            let levelDescription = this.tempLevelsName.slice(indexExist, indexExist + 1);

            const dialogRef = this.modal.confirm().size('lg').isBlocking(true).showClose(false).okBtn('Sim').cancelBtn('Não')
            .title('Editar definições').body('Tem a certeza que pretende remover o nível ' + levelDescription + '?').open();
            dialogRef.then(dialogRef => { dialogRef.result.then(result => {
              if (result) {
                let indexInUse: number = this.levelsInUseId.indexOf(level);
                if (indexInUse !== -1) {
                  this.failure = true;
                  this.success = false;
                  this.textFailure = 'Não foi possível remover o nível ' + this.levelsInUseName.slice(indexInUse, indexInUse + 1) + ' por se encontrar associado a pelo menos um exercício.';
                } else {
                  this.loading = true;
                  this.resourcesService.removeLevel(level).subscribe(
                      resRemove => {
                        this.failure = false;
                        this.success = true;
                        this.textSuccess = 'nível ' + levelDescription + ' removido com sucesso.';
                        console.log('Level removed. ');
                        this.resourcesService.fetchLevels().subscribe(
                        resLevels => {
                           this.levels = resLevels;
                           this.tempLevels();
                           this.loading = false;
                         }
                        );
                      },
                      err => {
                        this.failure = true;
                        this.success = false;
                        this.textFailure = 'Não foi possível remover o nível ' + levelDescription + '.';
                        console.error('Error removing level', err);
                        this.loading = false;
                      }
                    )
                }
              } else {
              }
            }).catch(() => {})});
          } else {
            this.failure = true;
            this.success = false;
            this.textFailure = 'Não foi possível remover o nível.';
            console.error('Error removing level');
            this.loading = false;
          }
      }

    saveChanges() {
      const dialogRef = this.modal.confirm().size('lg').isBlocking(true).showClose(false).okBtn('Sim').cancelBtn('Não')
      .title('Editar definições').body(`Tem a certeza que pretende guardar as alterações efectuadas?`).open();
      dialogRef.then(dialogRef => { dialogRef.result.then(result => {
        if (result) {
        } else {
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
