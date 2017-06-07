import { Component, OnInit } from '@angular/core';
import { Caregiver } from '../../models/caregiver';
import { Router } from '@angular/Router';

import { CaregiverService } from '../../services/caregiver/caregiver.service';


@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.css']
})
export class SignUpComponent {

  model: Caregiver = new Caregiver()

  genders = ['Female', 'Male', 'Other'];
  
  loading : boolean = false;
  error : string = undefined;

  constructor(private caregiverService: CaregiverService, private router: Router) { }

  createCaregiver() {
    this.loading = true;
    this.error = undefined;
    this.caregiverService.signUp(this.model)
      .subscribe(
        res => {
          this.loading = false;
          this.router.navigate(['/login']);
        },
        err => {
          this.error = err._body;
          this.loading = false;
          console.error('Error registering new caregiver. '+ err);
        }
    );
  }

  reset() {
    this.model = new Caregiver()
  }

}
