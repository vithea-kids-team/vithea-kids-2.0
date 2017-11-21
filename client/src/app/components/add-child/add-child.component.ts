import { Component, OnInit } from '@angular/core';
import { Child } from '../../models/child';
import { ChildrenService } from '../../services/children/children.service';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { IMyDpOptions, IMyDateModel, IMyDate } from 'mydatepicker';
import { Overlay } from 'ngx-modialog';
import { Modal } from 'ngx-modialog/plugins/bootstrap';
import { Location } from '@angular/common';

@Component({
  selector: 'app-add-child',
  templateUrl: './add-child.component.html',
  styleUrls: ['./add-child.component.css']
})

export class AddChildComponent implements OnInit {

  genders = ['Female', 'Male', 'Other'];
  model: Child = new Child();
  loading = false;
  loadingAdd = false;
  error: string = undefined;

  public myDatePickerOptions: IMyDpOptions = {
        dateFormat: 'dd/mm/yyyy'
  };

  public selDate: IMyDate;

  public usernameError;
  public genderError;
  public lastNameError;
  public firstNameError;
  public passwordError;
  public confirmpasswordError;
  public passwordLengthError;


  constructor(public modal: Modal, public childService: ChildrenService, public location: Location,
    public router: Router, public route: ActivatedRoute) { }

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
      if (this.model.password.length < 6 || this.model.password.length > 255) {
        this.passwordLengthError = true;
      } else {
        this.passwordLengthError = false;
        this.passwordError = false;
      }
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

  submit() {
    this.validateUsername();
    this.validatePassword();
    this.validateConfirmPassword();
    this.validateFirstName();
    this.validateLastName();
    this.validateGender();

    if (this.usernameError === false && this.passwordError === false && this.confirmpasswordError === false &&
      this.firstNameError === false && this.passwordLengthError === false && this.lastNameError === false && this.genderError === false) {
      this.createChild();
    }
  }

  submitEdit() {
    this.validateUsername();
    this.validateFirstName();
    this.validateLastName();
    this.validateGender();

    if (this.usernameError === false && this.firstNameError === false && this.lastNameError === false && this.genderError === false) {
      this.createChild();
    }
  }


  ngOnInit() {
    this.loading = true;
     this.route.params
      .switchMap((params: Params) => Observable.of(params))
      .subscribe(params => {
        const id: number = parseInt(params['childid'], 10);
        if (id) {
          this.childService.getChild(id).subscribe(
            res => {
                this.model = res;
                let d = new Date(res.birthDate);
                this.selDate = {year: d.getFullYear(), month: d.getMonth() + 1, day: d.getDate()};
                this.model.birthDate = d.toISOString();
                this.loading = false;
            },
            err => console.log('Error getting child')
          );
        } else {
          let d = new Date();
              this.selDate = {year: d.getFullYear(), month: d.getMonth() + 1, day: d.getDate()};
              this.model.birthDate = d.toISOString();
              this.loading = false;
        }
      });
  }

  createChild() {
    this.error = undefined;

    let childName = this.model.firstName + ' ' + this.model.lastName;

    if (this.model.childId) {
      const dialogRef = this.modal.confirm().size('lg').isBlocking(true).showClose(false).okBtn('Sim').cancelBtn('Não')
      .title('Editar criança').body('Tem a certeza que pretende editar os dados da criança ' + childName + '?').open();

      dialogRef.then(dialogRef => { dialogRef.result.then(result => {
        if (result) {
          this.childService.editChild(this.model).subscribe(
            res => {
              this.router.navigate(['/children/']);
              this.childService.setSuccess(true);
              this.childService.setFailure(false);
              this.childService.setTextSuccess('Criança ' + childName + ' editada com sucesso.');
            },
            err => {
              console.error('Error editing a child.', err);
              this.childService.setSuccess(false);
              this.childService.setFailure(true);
              this.childService.setTextFailure('Não foi possível editar a criança ' + childName + '.');
            }
          );
        } else {
          this.goBack();
        }
      }).catch(() => {});
    });
    } else {
      const dialogRef = this.modal.confirm().size('lg').isBlocking(true).showClose(false).okBtn('Sim').cancelBtn('Não')
      .title('Registar criança').body('Tem a certeza que pretende registar a criança ' + childName + '?').open();

      dialogRef.then(dialogRef => { dialogRef.result.then(result => {
        if (result) {
          this.childService.addChildren(this.model).subscribe(
            res => {
              this.router.navigate(['/children/']);
              this.childService.setSuccess(true);
              this.childService.setFailure(false);
              this.childService.setTextSuccess('Criança ' + childName + ' registada com sucesso.');
            },
            err => {
              console.error('Error registering new child.', err);
              this.router.navigate(['/children/']);
              this.childService.setSuccess(false);
              this.childService.setFailure(true);
              this.childService.setTextFailure('Não foi possível registar a criança ' + childName + '.');
            }
          );
        } else {
          this.goBack();
        }
      }).catch(() => {})});
    }
  }

  onDateChanged(event: IMyDateModel) {
    if (event.jsdate) {
      this.model.birthDate = event.jsdate.toISOString();
    }
  }

  goBack() {
    this.location.back();
  }
}
