import { Component, OnInit } from '@angular/core';
import { Child } from '../../models/child';
import { ChildrenService } from '../../services/children/children.service';
import { ActivatedRoute, Params, Router } from '@angular/Router';
import { Observable } from 'rxjs/Observable';
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

  constructor(public modal: Modal, public childService: ChildrenService, public location: Location,
    public router: Router, public route: ActivatedRoute) { }

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
