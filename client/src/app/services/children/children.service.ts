import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { Response } from '@angular/http';
import { HttpApiClient } from '../http/http-api-client.service';
import { Child } from '../../models/Child'

@Injectable()
export class ChildrenService {

  constructor(private http: HttpApiClient) { }

  getChildren() : Observable<Array<Child>> {
    return this.http.get('/app/listchildren')
      .map(result => result.json());
  }

  addChildren(child : Child) : Observable<Response>{
    child.birthDate = new Date(child.birthDate).toISOString()
    return this.http.post('/app/registerchild', JSON.stringify(child))
  }
}
