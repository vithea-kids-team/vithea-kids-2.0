import { Injectable } from '@angular/core';
import { Http, Headers } from '@angular/http'
import {RequestOptionsArgs, Connection, ConnectionBackend} from '@angular/http';
import { Configuration } from '../config/app.config';

@Injectable()
export class ChildrenService {

  constructor (private http : Http, private config : Configuration) {}

  getChildren() {

    let headers = new Headers();
    headers.append('Content-Type', 'application/json');
    let authToken = localStorage.getItem('auth_token');
    headers.append('Authorization', `Bearer ${authToken}`);

    
    /*return this.http.get(this.config.ServerWithApiUrl + 'listchildren', { headers })
            .map(result => result.json());*/

    return this.http.get(this.config.ServerWithApiUrl + 'listchildren')
            .map(result => result.json());
  }
}