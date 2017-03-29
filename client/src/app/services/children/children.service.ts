import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { Response } from '@angular/http';
import { HttpApiClient } from '../http/http-api-client.service';
import { Child } from '../../models/Child'
import { Router } from '@angular/Router';
import { find } from 'lodash';

@Injectable()
export class ChildrenService {

  children : Array<any>

  constructor(private http: HttpApiClient, private router : Router) { }

  getChildren() : Observable<Array<Child>> {
    return this.http.get('/listchildren')
      .map(result => this.children = result.json());
  }

  getChild(id) {
    return this.children.find(child => child.childId === id);
  }

  addChildren(child : Child) {
    child.birthDate = new Date(child.birthDate).toISOString()
    this.http.post('/registerchild', JSON.stringify(child))
      .subscribe(
        res => this.router.navigate(['/children']),
        err => {
          console.error("Error registering new child. "+ err);
        }
      );
  }
}
