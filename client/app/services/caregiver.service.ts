import { Injectable } from '@angular/core';
import { Http, Headers } from '@angular/http';
import { Configuration } from '../config/app.config';

@Injectable()
export class CaregiverService {
  private loggedIn = false;

  constructor(private http: Http, private config : Configuration) {
    this.loggedIn = !!localStorage.getItem('auth_token');
  }

  login(username, password) {
    let headers = new Headers();
    headers.append('Content-Type', 'application/json');

    return this.http
      .post(
        this.config.ServerWithApiUrl + 'login', 
        JSON.stringify({ username, password }), 
        { headers }
      )
      .map(res => res.json())
      .map((res) => {
        if (res.success) {
          localStorage.setItem('auth_token', res.auth_token);
          this.loggedIn = true;
        }

        return res.success;
      });
  }
  
  logout() {
    localStorage.removeItem('auth_token');
    this.loggedIn = false;
  }

  isLoggedIn() {
    return this.loggedIn;
  }
}