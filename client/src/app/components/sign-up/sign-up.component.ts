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

  public usernameError;
  public genderError;
  public lastNameError;
  public firstNameError;
  public emailError;
  public passwordError;
  public confirmpasswordError;

  constructor(public modal: Modal, public caregiverService: CaregiverService, public location: Location, public router: Router) { }


  validateUsername() {
    if (this.model.username === undefined) {
      this.usernameError = true;
    } else {
      this.usernameError = false;
      let username = this.model.username.replace(/\s+/g, '');
      if (username.length === 0) {
        this.usernameError = true;
      } else {
        this.usernameError = false;
      }
    }
  }

  validatePassword() {
    if (this.model.password === undefined) {
      this.passwordError = true;
    } else {
      this.passwordError = false;
    }
  }

  validateConfirmPassword() {
    this.confirmpasswordError = false

    if (this.model.confirmpassword === undefined) {
      this.confirmpasswordError = true;
    }

  }

  validateFirstName() {
    if (this.model.firstName === undefined) {
      this.firstNameError = true;
    } else {
      this.firstNameError = false;
      let firstName = this.model.firstName.replace(/\s+/g, '');
      if (firstName.length === 0) {
        this.firstNameError = true;
      } else {
        this.firstNameError = false;
      }
    }
  }

  validateLastName() {
    if (this.model.lastName === undefined) {
      this.lastNameError = true;
    } else {
      this.lastNameError = false;
      let lastName = this.model.lastName.replace(/\s+/g, '');
      if (lastName.length === 0) {
        this.lastNameError = true;
      } else {
        this.lastNameError = false;
      }
    }
  }

  validateGender() {
    if (this.model.gender === undefined) {
      this.genderError = true;
    } else {
      this.genderError = false;
    }
  }

  validateEmail() {
    if (this.model.email === undefined) {
      this.emailError = true;
    } else {
      this.emailError = false;
    }
  }

  submit() {
    this.validateUsername();
    this.validatePassword();
    this.validateConfirmPassword();
    this.validateFirstName();
    this.validateLastName();
    this.validateGender();
    this.validateEmail();

    if (this.usernameError === false && this.passwordError === false && this.confirmpasswordError === false &&
      this.firstNameError === false && this.lastNameError === false && this.genderError === false && this.emailError === false) {
      this.createCaregiver();
    }
  }

  // Not useful yet
  submitEdit() {
    this.validateUsername();
    this.validateFirstName();
    this.validateLastName();
    this.validateGender();
    this.validateEmail();

    if (this.usernameError === false && this.firstNameError === false && this.lastNameError === false && 
      this.genderError === false && this.emailError === false) {
      this.createCaregiver();
    }
  }

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
