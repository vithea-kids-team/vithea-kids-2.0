import { Component, OnInit } from '@angular/core';
import { Caregiver } from '../../models/caregiver';
import { Router } from '@angular/Router';
import { CaregiverService } from '../../services/caregiver/caregiver.service';
import { Overlay } from 'ngx-modialog';
import { Modal } from 'ngx-modialog/plugins/bootstrap';
import { Location } from '@angular/common';


@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.css']
})
export class SignUpComponent {

  model: Caregiver = new Caregiver()

  genders = ['Female', 'Male', 'Other'];

  loading: boolean = false;
  error: string = undefined;

  constructor(public modal: Modal, public caregiverService: CaregiverService, public location: Location, public router: Router) { }

  createCaregiver() {
    this.loading = true;
    this.error = undefined;
    let caregiverName = this.model.firstName + ' ' + this.model.lastName;

    const dialogRef = this.modal.confirm().size('lg').isBlocking(true).showClose(false).okBtn('Sim').cancelBtn('Não')
    .title('Registar cuidador').body('Tem a certeza que pretende registar o cuidador ' + caregiverName + '?').open();

    dialogRef.then(dialogRef => { dialogRef.result.then(result => {
      if (result) {
        this.caregiverService.signUp(this.model).subscribe(
          res => {
            this.caregiverService.setSuccess(true);
            this.caregiverService.setFailure(false);
            this.caregiverService.setTextSuccess('O cuidador ' + caregiverName + ' foi registado com sucesso.');
            this.router.navigate(['/login']);
            this.loading = false;
          },
          err => {
            this.error = err._body;
            console.error('Error registering new caregiver. ' + err);
            this.caregiverService.setFailure(true);
            this.caregiverService.setSuccess(false);
            this.caregiverService.setTextFailure('Não foi possível registar o cuidador ' + caregiverName + '.');
            this.router.navigate(['/home']);
            this.loading = false;
          }
        );
      } else {
        this.goBack();
      }
    }).catch(() => {})});
  }

  reset() {
    this.model = new Caregiver()
  }

  goBack() {
    this.location.back();
  }

}
