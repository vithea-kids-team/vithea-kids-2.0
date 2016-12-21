import { Injectable } from '@angular/core';
import { HttpApiClient } from '../http/http-api-client.service';
import { Router } from '@angular/Router'

@Injectable()
export class CaregiverService {

  private loggedIn = false;

  constructor(private http: HttpApiClient, private router: Router) { }

  login(username, password) {
    return this.http
      .post('/app/login', JSON.stringify({ username, password }))
      .subscribe(res => {
        if (res) {
          this.loggedIn = true;          
          localStorage.setItem("Authorization", res.json().authToken)
          this.router.navigate(['/children'])
        }
      });
  }

  logout() {
    this.loggedIn = false;
  }

  isLoggedIn() {
    return this.loggedIn;
  }
}
