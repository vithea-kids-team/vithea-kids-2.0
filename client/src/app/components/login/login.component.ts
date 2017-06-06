import { Component } from '@angular/core';

import { CaregiverService } from '../../services/caregiver/caregiver.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  model: any = {};
  loading: boolean = false;
  error: string = undefined;

  constructor(private caregiverService: CaregiverService) { }

  login() {
    this.loading = true;
    this.error = undefined;
    this.caregiverService.login(this.model.username, this.model.password)
    .subscribe(res => {
      this.loading = false;
    },
    err => {
      this.loading = false;
      this.error = err._body;
    });
  }
}
