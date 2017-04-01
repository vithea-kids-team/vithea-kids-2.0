import 'rxjs/add/operator/map'
import { Injectable } from '@angular/core';
import { Observable} from 'rxjs/Observable';
import { HttpApiClient } from '../http/http-api-client.service';
import { Router } from '@angular/Router';
import { Caregiver } from '../../models/Caregiver';


@Injectable()
export class CaregiverService {

  private userObs : Observable<any>;
  private failedLogin : boolean = false;
  private loggedIn : boolean = false;
  private username : string;

  constructor(private http: HttpApiClient, private router: Router) { }

  login(username, password) {
    return this.http
      .post('/login', JSON.stringify({ username, password }))
      .subscribe(res => {
          this.failedLogin = false;
          this.loggedIn = true;
          this.username = username;          
          localStorage.setItem("Authorization", res.json().authToken)
          this.router.navigate(['/children']);
      }, err => {
        this.failedLogin = true;
        console.error('Login error.', err);
      });
  }

  signUp(caregiver : Caregiver) {
    return this.http.post('/signup', JSON.stringify(caregiver))
    .subscribe(
      res => this.router.navigate(['/login']),
      err => {
        console.error("Error registering new caregiver. "+ err);
      }
    );
  }

  logout() {
    localStorage.removeItem('Authorization');
    this.loggedIn = false;
    this.username = undefined;
    this.router.navigate(['/home']);
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

  whoAmI(): Observable<any>{
    if(!this.userObs){
      this.userObs = this.http.get('/me')
                                   .map(res => res.json());
                                   /*.publishReplay(10)
                                   .refCount();*/
    }
    return this.userObs;
  }

  getUsername() {
    return this.username;
  }
}
