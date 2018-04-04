import { Component, OnInit, OnChanges} from '@angular/core';
import { CaregiverService } from '../../services/caregiver/caregiver.service';
import { Password } from '../../models/password';
import { Location } from '@angular/common';
import { Overlay } from 'ngx-modialog';
import { Modal } from 'ngx-modialog/plugins/bootstrap';
import { Router } from '@angular/router';

import { Observable} from 'rxjs/Rx';

@Component({
  selector: 'app-login',
  templateUrl: './recover-password.component.html',
  styleUrls: ['./recover-password.component.css']
})
export class RecoverPasswordComponent implements OnInit, OnChanges {

  model: Password = new Password()
  loading = false;
  error: string = undefined;
  failurePassword = false;
  textFailurePassword;
  failure = false;
  textFailure;
  success = false;
  textSuccess;
  usernameOk = false;
  usernameError;
  answerError;
  username;
  password;
  passwordError;
  confirmpasswordError;
  securityQuestion;
  securityAnswer;

  constructor(public modal: Modal, public router: Router, public caregiverService: CaregiverService, public location: Location) { }


  validateUsername() {
    if (this.username === undefined) {
      this.usernameError = true;
    } else {
      this.usernameError = false;
      let username = this.username.replace(/\s+/g, '');
      if (username.length < 3) {
        this.usernameError = true;
      } else {
        this.usernameError = false;
      }
    }
  }

  validateAnswer() {
    if (this.securityAnswer === undefined) {
      this.answerError = true;
    } else {
      this.answerError = false;
      let securityAnswer = this.securityAnswer.replace(/\s+/g, '');
      if (securityAnswer.length === 0) {
        this.answerError = true;
      } else {
        this.answerError = false;
      }
    }
  }

  validatePassword() {
    if (this.model.password === undefined) {
      this.passwordError = true;
    } else {
      if (this.model.password.length < 6 || this.model.password.length > 255) {
        this.passwordError = true;
      } else {
        this.passwordError = false;
      }
    }
  }

  validateConfirmPassword() {
    if (this.model.confirmpassword === undefined) {
      this.confirmpasswordError = true;
    } else {
      if (this.model.password !== this.model.confirmpassword) {
        this.confirmpasswordError = true;
      } else {
        this.confirmpasswordError = false;
      }
    }
  }

  ngOnInit() {
  }

  ngOnChanges() {
    this.failurePassword = this.caregiverService.getFailurePassword();
    this.textFailurePassword = this.caregiverService.getTextFailurePassword();
    this.failure = this.caregiverService.getFailure();
    this.textFailure = this.caregiverService.getTextFailure();
    this.success = this.caregiverService.getSuccess();
    this.textSuccess = this.caregiverService.getTextSuccess();
    console.log(this.failure)
  }

  reset() {
    this.caregiverService.failurePassword = false;
    this.caregiverService.textFailurePassword = '';
    this.caregiverService.failure = false;
    this.caregiverService.textFailure = '';
    this.caregiverService.success = false;
    this.caregiverService.textSuccess = '';
  }

  submit() {
    this.validateUsername();

    if (this.usernameError === false) {
      const dialogRef = this.modal.confirm().size('lg').isBlocking(true).showClose(false).okBtn('Sim').cancelBtn('Não')
      .title('Recuperar password').body('Tem a certeza que pretende validar o nome do cuidador ' + this.username + '?').open();

      dialogRef.then(dialogRef2 => { dialogRef2.result.then(result => {
        if (result) {
          this.caregiverService.fetchQuestion(this.username).subscribe(
            res => {
              this.failure = false;
              this.success = true;
              this.textSuccess = 'O nome do cuidador ' + this.username + ' foi validado com sucesso.'
              this.usernameOk = true;
              this.securityQuestion = res.json().securityQuestion;
              this.loading = false;
            },
            err => {
              console.error('Error validating username. ' + err);
              this.failure = true;
              this.success = false;
              this.textFailure = 'O cuidador com o nome ' + this.username + ' não existe.';
              this.usernameOk = false;
              this.loading = false;
            }
          );
        } else {
          this.goBack();
        }
      }).catch(() => {})});
    }
  }

  recoverPassword() {
    this.validateUsername();
    this.validateAnswer();
    this.validatePassword();
    this.validateConfirmPassword();

    if (this.usernameError === false && this.answerError === false && this.passwordError === false && this.confirmpasswordError === false) {
      const dialogRef = this.modal.confirm().size('lg').isBlocking(true).showClose(false).okBtn('Sim').cancelBtn('Não')
      .title('Recuperar password').body('Tem a certeza que pretende recuperar a password do cuidador ' + this.username + '?').open();

      dialogRef.then(dialogRef2 => { dialogRef2.result.then(result => {
        if (result) {
          this.caregiverService.recoverPassword(this.username, this.securityAnswer, this.model.password).subscribe(
            res => {
              console.log(res);
              this.caregiverService.setFailure(false);
              this.caregiverService.setSuccess(true);
              this.caregiverService.setTextSuccess('A password do cuidador ' + this.username + ' foi recuperada com sucesso.')
              this.loading = false;
              this.router.navigate(['/home']);
            },
            err => {
              console.error('Error recovering pasword. ' + err);
              this.failure = true;
              this.success = false;
              this.textFailure = 'A resposta de segurança introduzida para o cuidador ' + this.username + ' encontra-se errada.';
              this.loading = false;
            }
          );
        } else {
          this.goBack();
        }
      }).catch(() => {})});
    }
  }

  goBack() {
    this.location.back();
  }
}
