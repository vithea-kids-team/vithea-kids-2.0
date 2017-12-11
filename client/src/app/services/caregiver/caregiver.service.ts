import 'rxjs/add/operator/map'
import { Injectable } from '@angular/core';
import { Observable} from 'rxjs/Rx';
import { HttpApiClient } from '../http/http-api-client.service';
import { Router } from '@angular/router';
import { Caregiver } from '../../models/caregiver';
import { Overlay } from 'ngx-modialog';
import { Modal } from 'ngx-modialog/plugins/bootstrap';
import { Location } from '@angular/common';


@Injectable()
export class CaregiverService {

  userObs: Observable<any>;
  failedLogin: boolean = false;
  loggedIn: boolean = false;
  success = false
  failure = false;
  textSuccess;
  textFailure;

  constructor(public http: HttpApiClient, public router: Router, public location: Location,public modal: Modal) { }

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
      .post('/login/caregiver', JSON.stringify({ username, password }))
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

    const dialogRef = this.modal.confirm().size('lg').isBlocking(true).showClose(false).okBtn('Sim').cancelBtn('Não')
    .title('Sair').body('Tem a certeza que pretende terminar a sessão actual?').open();

    dialogRef.then(dialogRef => { dialogRef.result.then(result => {
      if (result) {
        return this.http.post('/logout', JSON.stringify(null)).subscribe(
          res => {
            if (res) {
              localStorage.removeItem('Username')
              localStorage.removeItem('Authorization');
              this.loggedIn = false;
              this.router.navigate(['/home']);
            }
          },
          err => {
            console.error('Error logging out. ' + err);
          }
        );
      } else {
        this.goBack();
      }
    }).catch(() => {})});
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

  goBack() {
    this.location.back();
  }
}
