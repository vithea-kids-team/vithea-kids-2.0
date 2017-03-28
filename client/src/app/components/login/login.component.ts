import { Component } from '@angular/core';

import { CaregiverService } from '../../services/caregiver/caregiver.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  model: any = {};
  failedLogin : boolean = false;

  constructor(private caregiverService: CaregiverService) { }

  login() {
    this.caregiverService.login(this.model.username, this.model.password);
    this.failedLogin = !this.caregiverService.isLoggedIn();
  }
}
