import { Component } from '@angular/core';
import { Router } from '@angular/router';

import { CaregiverService } from '../../services/caregiver/caregiver.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  constructor(private caregiverService: CaregiverService) { }

  login(event, username, password) {
    this.caregiverService.login(username, password)
  }
}
