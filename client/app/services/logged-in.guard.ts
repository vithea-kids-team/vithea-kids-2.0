import { Injectable } from '@angular/core';
import { Router, CanActivate } from '@angular/router';
import { CaregiverService } from './caregiver.service';

@Injectable()
export class LoggedInGuard implements CanActivate {
  constructor(private caregiver: CaregiverService) {}

  canActivate() {
    return this.caregiver.isLoggedIn();
  }
}