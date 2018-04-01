import { Component, OnInit } from '@angular/core';
import { Caregiver } from '../../models/caregiver';
import { Router } from '@angular/router';
import { CaregiverService } from '../../services/caregiver/caregiver.service';
import { Overlay } from 'ngx-modialog';
import { Modal } from 'ngx-modialog/plugins/bootstrap';
import { Location } from '@angular/common';


@Component({
  selector: 'app-edit-caregiver',
  templateUrl: './edit-caregiver.component.html',
  styleUrls: ['./edit-caregiver.component.css']
})
export class EditCaregiverComponent  implements OnInit {

  model: Caregiver = new Caregiver()

  genders = ['Female', 'Male', 'Other'];
  securityquestions = ['Qual o seu grupo musical favorito quando era criança?', 'Qual foi o primeiro filme que viu no cinema?', 
  'Qual o apelido do seu professor do 3º ano?', 'Qual o nome do seu segundo animal de estimação?'];

  loading = false;
  error: string = undefined;

  public usernameError;
  public genderError;
  public lastNameError;
  public firstNameError;
  public emailError;
  public passwordError;
  public passwordLengthError;
  public confirmpasswordError;
  public securityQuestionError;
  public securityPasswordError;

  // temp vars
  public firstName;
  public lastName;
  public gender;
  public confirmpassword: string
  public email: string
  public securityQuestion: string
  public securityPassword: string

  constructor(public modal: Modal, public caregiverService: CaregiverService, public location: Location, public router: Router) { }

  ngOnInit() {
    this.loading = true;
  


  }

  validateSecurityQuestion() {
    if (this.model.securityQuestion === undefined) {
      this.securityQuestionError = true;
    } else {
      this.securityQuestionError = false;
      let securityQuestion = this.model.securityQuestion.replace(/\s+/g, '');
      if (securityQuestion.length === 0) {
        this.securityQuestionError = true;
      } else {
        this.securityQuestionError = false;
      }
    }
  }

  validateSecurityPassword() {
    if (this.model.securityPassword === undefined) {
      this.securityPasswordError = true;
    } else {
      this.securityPasswordError = false;
      let securityAnswer = this.model.securityPassword.replace(/\s+/g, '');
      if (securityAnswer.length === 0) {
        this.securityPasswordError = true;
      } else {
        this.securityPasswordError = false;
      }
    }
  }

  validatePassword() {
    if (this.model.password === undefined) {
      this.passwordError = true;
    } else {
      if (this.model.password.length < 6 || this.model.password.length > 255) {
        this.passwordLengthError = true;
      } else {
        this.passwordLengthError = false;
        this.passwordError = false;
      }
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
    // this.validatePassword();
    this.validateFirstName();
    this.validateLastName();
    this.validateGender();
    this.validateEmail();
    this.validateSecurityQuestion();
    this.validateSecurityPassword();

    if (this.firstNameError === false && this.lastNameError === false && this.genderError === false && this.emailError === false &&
      this.securityQuestionError === false) { // && this.securityPasswordError === false) {
      this.editCaregiver();
    }
  }

  editCaregiver() {
    this.loading = true;
    this.error = undefined;
    let caregiverName = this.model.firstName + ' ' + this.model.lastName;

    const dialogRef = this.modal.confirm().size('lg').isBlocking(true).showClose(false).okBtn('Sim').cancelBtn('Não')
    .title('Editar cuidador').body('Tem a certeza que pretende editar o cuidador ' + caregiverName + '?').open();

    dialogRef.then(dialogRef2 => { dialogRef2.result.then(result => {
      if (result) {
        this.caregiverService.editCaregiver(this.model).subscribe(
          res => {
            this.caregiverService.setSuccess(true);
            this.caregiverService.setFailure(false);
            this.caregiverService.setTextSuccess('O cuidador ' + caregiverName + ' foi editado com sucesso.');
            this.router.navigate(['/home']);
            this.loading = false;
          },
          err => {
            this.error = err._body;
            console.error('Error registering new caregiver. ' + err);
            this.caregiverService.setFailure(true);
            this.caregiverService.setSuccess(false);
            this.caregiverService.setTextFailure('Não foi possível editar o cuidador ' + caregiverName + '.');
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
