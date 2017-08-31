import { Component, OnInit} from '@angular/core';
import { CaregiverService } from '../../services/caregiver/caregiver.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  model: any = {};
  loading: boolean = false;
  error: string = undefined;
  success = false;
  textSuccess;

  constructor(public caregiverService: CaregiverService) { }

  ngOnInit() {
    this.success = this.caregiverService.getSuccess();
    this.textSuccess = this.caregiverService.getTextSuccess();
  }

  login() {
    this.loading = true;
    this.error = undefined;
    this.caregiverService.login(this.model.username, this.model.password).subscribe(res => {
      this.loading = false;
    },
    err => {
      this.loading = false;
      this.error = err._body;
    });
  }

  reset() {
    this.caregiverService.success = false;
    this.caregiverService.textSuccess = '';
  }
}
