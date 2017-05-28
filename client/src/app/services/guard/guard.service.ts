import { Injectable } from '@angular/core';
import { CanActivate } from '@angular/router';
import { Router } from '@angular/Router';

import { CaregiverService } from '../caregiver/caregiver.service';


@Injectable()
export class GuardService implements CanActivate {

  constructor(private caregiverService : CaregiverService, private router : Router) { }

  canActivate() {

    if (this.caregiverService.isLoggedIn()) {
      return true;
    } else {
      this.router.navigate(['/login']);
      return false;
    }
  }

}