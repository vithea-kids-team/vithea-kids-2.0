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
    this.fetchTopics();
    this.fetchLevels();
    this.fetchResources();
  }

  fetchResources() {
     /* fetch resources */
    this.http.get('/listresources')
      .map(result => result.json())
      .subscribe(
        result => {
          result.forEach((resource : Resource) => {
            this.resources[resource.resourceArea.toLowerCase()].push(resource);
          });
        }, 
        err => console.error("Error getting resources.")
      )

    this.http.get('/listanimatedcharacters')
      .map(result => result.json())
      .subscribe(
        result => {
          this.resources.animatedcharacter = result;
        }, 
        err => console.error("Error getting resources.")
      )
  }

  fetchLevels() {
    /* fetch levels */
    this.http.get('/listlevels')
      .map(result => result.json())
      .subscribe(
        result => this.levels = result, 
        err => console.error("Error getting levels.")
      )
  }

  fetchTopics() {
     /* fetch topics */
    this.http.get('/listtopics')
      .map(result => result.json())
      .subscribe(
        result => this.topics = result,
        err => console.error("Error getting topics.")
      );
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
