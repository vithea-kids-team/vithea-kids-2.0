import { Injectable } from '@angular/core';
import { HttpApiClient } from '../http/http-api-client.service';
import { Topic } from '../../models/topic';
import { Level } from '../../models/level';
import { Resource } from '../../models/resource';

@Injectable()
export class ResourcesService {

  public topics: Array<Topic>
  public levels: Array<Level>
  public resources: Array<Resource>

  constructor(private http: HttpApiClient) { 

    /* fetch topics */
    this.http.get('/app/listtopics')
      .map(result => result.json())
      .subscribe(
        result => this.topics = result,
        err => console.error("Error getting topics.")
      );

    /* fetch levels */
    this.http.get('/app/listlevels')
      .map(result => result.json())
      .subscribe(
        result => this.levels = result, 
        err => console.error("Error getting levels.")
      )

    /* fetch resources */
    this.http.get('/app/listresources')
      .map(result => result.json())
      .subscribe(
        result => this.resources = result, 
        err => console.error("Error getting resources.")
      )

  }
}
