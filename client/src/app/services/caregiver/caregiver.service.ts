import { Injectable } from '@angular/core';
import { HttpApiClient } from '../http/http-api-client.service';
import { Router } from '@angular/Router';
import { Caregiver } from '../../models/Caregiver';

@Injectable()
export class CaregiverService {

  private loggedIn = false;

  constructor(private http: HttpApiClient, private router: Router) { }

  login(username, password) {
    return this.http
      .post('/app/login', JSON.stringify({ username, password }))
      .subscribe(res => {
        if (res && res.status != 401) {
          this.loggedIn = true;          
          localStorage.setItem("Authorization", res.json().authToken)
          this.router.navigate(['/children'])
        }
      }, err => {
        console.error('Wrong username password combination.');
      });
  }

  signUp(caregiver : Caregiver) {
    return this.http.post('/app/signup', JSON.stringify(caregiver))
    .subscribe(
      res => this.router.navigate(['/login']),
      err => {
        console.error("Error registering new caregiver. "+ err);
      }
    );
  }

  logout() {
    this.loggedIn = false;
  }

  isLoggedIn() {
    return this.loggedIn;
  }
}
