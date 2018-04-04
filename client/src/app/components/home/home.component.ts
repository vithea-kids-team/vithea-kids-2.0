import { Component, OnInit } from '@angular/core';
import { CaregiverService } from '../../services/caregiver/caregiver.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  failure = false;
  textFailure;
  success = false;
  textSuccess;

  constructor(public caregiverService: CaregiverService) { }

  ngOnInit() {
    this.failure = this.caregiverService.getFailure();
    this.textFailure = this.caregiverService.getTextFailure();
    this.success = this.caregiverService.getSuccess();
    this.textSuccess = this.caregiverService.getTextSuccess();
  }

  reset() {
    this.caregiverService.failure = false;
    this.caregiverService.textFailure = '';
    this.caregiverService.success = false;
    this.caregiverService.textSuccess = '';
  }

}
