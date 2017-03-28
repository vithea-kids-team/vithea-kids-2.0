import { Injectable } from '@angular/core';
import { HttpApiClient } from '../http/http-api-client.service';
import { Router } from '@angular/Router';
import { Caregiver } from '../../models/Caregiver';

@Injectable()
export class CaregiverService {

  private loggedIn : boolean = false;
  private username : string;

  constructor(private http: HttpApiClient, private router: Router) { }

  login(username, password) {
    return this.http
      .post('/login', JSON.stringify({ username, password }))
      .subscribe(res => {
        if (res && res.status != 401) {
          this.loggedIn = true;
          this.username = username;          
          localStorage.setItem("Authorization", res.json().authToken)
          this.router.navigate(['/children'])
        }
      }, err => {
        console.error('Wrong username password combination.');
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
    this.loggedIn = false;
    this.username = undefined;
    this.router.navigate(['/home']);
  }

  isLoggedIn() {
    return this.loggedIn;
  }

  getUsername() {
    return this.username;
  }
}
