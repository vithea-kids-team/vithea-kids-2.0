import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { Response } from '@angular/http';
import { HttpApiClient } from '../http/http-api-client.service';
import { Child } from '../../models/Child'
import { find } from 'lodash';

@Injectable()
export class ChildrenService {

  children: Array<any>
  currentChild: any
  currentSequence: any

  constructor(private http: HttpApiClient) { }

  getChildren(): Observable<Array<Child>> {
    return this.http.get('/listchildren')
      .map(result => this.children = result.json());
  }

  getChild(id: number) {
    return this.http.get('/child/' + id)
      .map(result => result.json());
  }

  addChildren(child: Child) : Observable<Response> {
    child.birthDate = new Date(child.birthDate).toISOString()
    return this.http.post('/registerchild', JSON.stringify(child));
  }

  editChild(child:Child) : Observable<Response> {
    child.birthDate = new Date(child.birthDate).toISOString()
    return this.http.post('/editchild/'+ child.childId, JSON.stringify(child));
  }

  deleteChild(id: number) {
    return this.http.delete('/deletechild/' + id);
  }

  getChildSequences(id: number) {     
    return this.http.get('/child/'+ id + '/sequences')
      .map(res => res.json());
  }

  getSequence(id: number) {
      return this.http.get('/sequences/' + id);
  }

  getChildMessages(id : number) {
      return this.http.get('/child/'+ id + '/personalmessages')
        .map(res => res.json());
  }

  setPersonalMessages(childId, greetingMessage, exerciseReinforcementMessage, sequenceReinforcementMessage) {
    const messages = {
      greetingMessage: greetingMessage,
      exerciseReinforcementMessage: exerciseReinforcementMessage,
      sequenceReinforcementMessage: sequenceReinforcementMessage
    };

    return this.http.post('/child/'+ childId + '/personalmessages', JSON.stringify(messages));
  }

}
