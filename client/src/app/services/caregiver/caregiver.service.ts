import 'rxjs/add/operator/map'
import { Injectable } from '@angular/core';
import { Observable} from 'rxjs/Rx';
import { HttpApiClient } from '../http/http-api-client.service';
import { Router } from '@angular/router';
import { Caregiver } from '../../models/caregiver';


@Injectable()
export class CaregiverService {

  userObs: Observable<any>;
  failedLogin: boolean = false;
  loggedIn: boolean = false;
  success = false
  failure = false;
  textSuccess;
  textFailure;

  constructor(public http: HttpApiClient, public router: Router) { }

  getSuccess() {
    return this.success;
  }
  getFailure() {
    return this.failure;
  }
  getTextSuccess() {
    return this.textSuccess;
  }
  getTextFailure() {
    return this.textFailure;
  }
  setSuccess(success: boolean) {
    this.success = success;
  }
  setFailure(failure: boolean) {
    this.failure = failure;
  }
  setTextSuccess(text: string) {
    this.textSuccess = text;
  }
  setTextFailure(text: string) {
    this.textFailure = text;
  }

  login(username, password) {
    return this.http
      .post('/login', JSON.stringify({ username, password }))
      .map(res => {
          if (res) {
            this.failedLogin = false;
            this.loggedIn = true;
            localStorage.setItem('Username', username);
            localStorage.setItem('Authorization', res.json().authToken)
            this.router.navigate(['/home']);
          } else {
            this.failedLogin = true;
            console.error('Login error. No token returned');
          }
      }, err => {
        this.failedLogin = true;
        console.error('Login error.', err);
      });
  }

  signUp(caregiver: Caregiver) {
    return this.http.post('/signup', JSON.stringify(caregiver));
  }

  logout() {
    localStorage.removeItem('Username')
    localStorage.removeItem('Authorization');
    this.loggedIn = false;
    this.router.navigate(['/home']);

    return this.http.post('/logout', JSON.stringify(null)).subscribe(
      res => res,
      err => {
        console.error('Error logging out. ' + err);
      }
    );
  }

  isLoggedIn() {
    return  !!this.getJWT();
  }

  failedToLogin() {
    return this.failedLogin;
  }

  getJWT() {
    return localStorage.getItem('Authorization');
  }

  getUsername() {
    return localStorage.getItem('Username');
  }
}
