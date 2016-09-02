import { Injectable } from '@angular/core';
import { Http } from '@angular/http'
import {RequestOptionsArgs, Connection, ConnectionBackend} from '@angular/http';
import { Configuration } from '../config/app.config';

@Injectable()
export class ChildrenService {

  constructor (private http : Http, private config : Configuration) {}

  getChildren() {
    return this.http.get(this.config.ServerWithApiUrl + 'listchildren')
            .map(result => result.json());
  }
}