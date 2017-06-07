import 'rxjs/add/operator/map'
import { Injectable } from '@angular/core';
import { Observable} from 'rxjs/Observable';
import { HttpApiClient } from '../http/http-api-client.service';
import { Router } from '@angular/Router';
import { Caregiver } from '../../models/caregiver';


@Injectable()
export class CaregiverService {

  private userObs : Observable<any>;
  private failedLogin : boolean = false;
  private loggedIn : boolean = false;

  constructor(private http: HttpApiClient, private router: Router) { }

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

  signUp(caregiver : Caregiver) {
    return this.http.post('/signup', JSON.stringify(caregiver));
  }

  logout() {
    return this.http.post('/logout', JSON.stringify(null))
    .subscribe(
      res => {
        localStorage.removeItem('Username')
        localStorage.removeItem('Authorization');
        this.loggedIn = false;
        this.router.navigate(['/home']);
      },
      err => {
        console.error('Error loggin out. '+ err);
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
