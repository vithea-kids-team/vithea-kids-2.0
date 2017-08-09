import { Injectable } from '@angular/core';
import { HttpApiClient } from '../http/http-api-client.service';
import { Topic } from '../../models/topic';
import { Level } from '../../models/level';
import { Resource } from '../../models/resource';

@Injectable()
export class ResourcesService {

  public topics: Array<Topic> = [];
  public levels: Array<Level> = [];
  public stimuli: Array<Resource> = [];
  public resources = {
    stimuli: [],
    answers: [],
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
     /* fetch resources */
    return this.http.get('/listresources')
      .map(result => {
        result && result.json().forEach((resource : Resource) => {
            this.resources[resource.resourceArea.toLowerCase()].push(resource);
          });
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
    return this.http.get('/listanimatedcharacters')
      .map(result => this.resources.animatedcharacter = result && result.json());
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

  getResourcesByType(type: string, selected = 0) {
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
