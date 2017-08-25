import { Injectable } from '@angular/core';
import { HttpApiClient } from '../http/http-api-client.service';
import { Observable } from 'rxjs/Observable';
import { Topic } from '../../models/topic';
import { Level } from '../../models/level';
import { Resource } from '../../models/resource';

@Injectable()
export class ResourcesService {

  public topics: Array<Topic> = [];
  public levels: Array<Level> = [];
  public tempReinforcement: Array<Resource> = [];
  public tempStimuli: Array<Resource> = [];

  public resources = {
    stimuli: [],
    reinforcement: [],
    animatedcharacter: []
  }

  constructor(public http: HttpApiClient) {
    this.fetchTopics().subscribe();
    this.fetchLevels().subscribe();
    this.fetchResources().subscribe();
    this.fetchAnimatedCharacters().subscribe();
  }

  fetchResources() {
    return this.http.get('/listresources').map(result => {
        result && result.json().forEach((resource : Resource) => {
          let type: string = resource.resourceArea.toLowerCase();
          if (type === 'stimuli') {
            this.tempStimuli.push(resource);
          } else if (type === 'reinforcement'){
            this.tempReinforcement.push(resource);
          }
        });
        this.resources['stimuli'] = this.tempStimuli;
        this.resources['reinforcement'] = this.tempReinforcement;
        this.tempStimuli = [];
        this.tempReinforcement = [];
      });
  }

  fetchLevels() {
    /* fetch levels */
    return this.http.get('/listlevels').map(result => this.levels = result && result.json());
  }

  fetchTopics() {
     /* fetch topics */
    return this.http.get('/listtopics').map(result => this.topics = result && result.json());
  }

  fetchAnimatedCharacters() {
    return this.http.get('/listanimatedcharacters').map(result => this.resources.animatedcharacter = result && result.json());
  }

  uploadFiles(files, type, name?) {
    if (type === 'animatedcharacter') {
      return this.http.upload('/uploadanimatedcharacter/' + name, files);
    } else {
      return this.http.upload('/uploadresources/' + type, files);
    }
  }

  addTopic(newTopic: string) {
    console.log('Adicionar ' + newTopic);
    let body = {
      newTopic: newTopic
    };
    return this.http.put('/addtopic', JSON.stringify(body));
  }

  addLevel(newLevel: string) {
    console.log('Adicionar ' + newLevel);
    let body = {
      newLevel: newLevel
    };
    return this.http.put('/addlevel', JSON.stringify(body));
  }

  removeTopic(topic: number) {
    console.log('Remover ' + topic);
    return this.http.delete('/removetopic/' + topic);
  }

  removeLevel(level: number) {
    console.log('Remover ' + level);
    return this.http.delete('/removelevel/' + level);
  }

  getResourcesByType(type: string) {
    let resources = this.resources;
    let resourcesType = resources[type.toLowerCase()];

    let results = [];
    for (let i = 0; i < resourcesType.length; i++) {
      results.push(Object.assign({}, resources[type][i]));
    }

    return results;
  }

  removeResource(resourceId: number) {
    return this.http.delete('/removeresource/' + resourceId);
  }

  getResourcesByTypeSelected(type: string, selected = 0) {
    let resources = this.resources;
    type = type.toLowerCase();

    let result = [];
    if (resources[type] && resources[type].length > 0) {
      for (let i = 0; i < resources[type].length; i++) {
        // value copy
        result.push(Object.assign({}, resources[type][i]));
        if (selected && resources[type][i].resourceId === selected) {
          result[i].selected = true;
        }
      }
    }
    return result;
  }
}
