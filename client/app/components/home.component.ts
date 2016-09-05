import { Component } from '@angular/core';
import { CORE_DIRECTIVES } from '@angular/common';
import { Http, Headers } from '@angular/http';
import { Router } from '@angular/router';
import { AUTH_PROVIDERS } from 'angular2-jwt';

@Component({
  selector: 'home',
  directives: [ CORE_DIRECTIVES ],
  providers: [ AUTH_PROVIDERS ],
  templateUrl: 'app/html/home.html'
})
export class HomeComponent {
  jwt: string;
  decodedJwt: string;
  
  constructor(public router: Router, public http: Http) {
    this.jwt = localStorage.getItem('id_token');
  }

  logout() {
    localStorage.removeItem('id_token');
    this.router.navigate(['/login']);
  }
}