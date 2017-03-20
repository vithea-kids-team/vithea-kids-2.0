import { Component, OnInit } from '@angular/core';
import { Caregiver } from '../../models/Caregiver';

import { CaregiverService } from '../../services/caregiver/caregiver.service';


@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.css']
})
export class SignUpComponent {

  model: Caregiver = new Caregiver()

  genders = ['Female', 'Male', 'Other'];

  constructor(private caregiverService: CaregiverService) { }

  createCaregiver() {
    this.caregiverService.signUp(this.model);
  }

  reset() {
    this.model = new Caregiver()
  }

}
