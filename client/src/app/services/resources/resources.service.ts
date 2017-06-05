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

  constructor(private http: HttpApiClient) { 
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
    return this.http.get('/listlevels')
      .map(result => this.levels = result && result.json());
  }

  fetchTopics() {
     /* fetch topics */
    return this.http.get('/listtopics')
      .map(result => this.topics = result && result.json());
  }

  fetchAnimatedCharacters() {
    return this.http.get('/listanimatedcharacters')
      .map(result => this.resources.animatedcharacter = result && result.json());
  }

  uploadFiles(files, type, name?) {
    if (type === 'animatedcharacter') {
      return this.http.upload('/uploadanimatedcharacter/'+ name, files);
    } else {
      return this.http.upload('/uploadresources/' + type, files);
    }
  }

  addTopic(newTopic : string) {
    let body = {
      newTopic: newTopic
    };
    return this.http.put('/addtopic', JSON.stringify(body));
  }

  addLevel(newLevel : string) {
    let body = {
      newLevel: newLevel
    };
    return this.http.put('/addlevel', JSON.stringify(body));
  }

  removeTopic(topic : number) {
    return this.http.delete('/removetopic/'+ topic);
  }

  removeLevel(level : number) {
    return this.http.delete('/removelevel/'+ level);
  }
}
