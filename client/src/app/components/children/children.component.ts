import 'rxjs/add/operator/switchMap';

import { Component, OnInit, OnChanges } from '@angular/core';
import { ActivatedRoute, Params }   from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { Child } from '../../models/child';
import { ChildrenService } from '../../services/children/children.service';
import { Location } from '@angular/common';
import { PaginationService } from '../../services/pagination/pagination.service';
import { Overlay } from 'ngx-modialog';
import { Modal } from 'ngx-modialog/plugins/bootstrap';

@Component({
  selector: 'app-children',
  templateUrl: './children.component.html',
  styleUrls: ['./children.component.css']
})
export class ChildrenComponent implements OnInit, OnChanges {

  public children: Array<Child>;
  public sequenceId: number = 0;
  public sequence;
  public searchBy: string = '';
  public loading: boolean = false;
  public success = false;
  public failure = false;
  public textSuccess;
  public textFailure;

  // pager object
  pager: any = {};

  // paged items
  pagedItems: any[];

  constructor(public route: ActivatedRoute, public childrenService: ChildrenService, public location: Location,
    public paginationService: PaginationService, public modal: Modal) { }

  ngOnInit() {
    this.fetchChildren();
  }

  ngOnChanges() {
    this.fetchChildren();
  }

  public updateSuccessFailure() {
    this.success = this.childrenService.getSuccess();
    this.failure = this.childrenService.getFailure();
    this.textSuccess = this.childrenService.getTextSuccess();
    this.textFailure = this.childrenService.getTextFailure();
  }

  fetchChildren() {
    this.loading = true;
    this.updateSuccessFailure();

    this.route.params
      .switchMap((params: Params) => Observable.of(params))
      .subscribe(params => {
        const id: number = parseInt(params['sequenceid'], 10);
        if (id) {
          this.sequenceId = id;
          this.getSequenceChildren(id);
        } else {
          this.getChildren();
        }
      },
      err => {
        console.error('Error loading children', err);
        this.loading = false;
      });
  }

  getChildren() {
    this.childrenService.getChildren().subscribe(
      result => {
        this.children = result;
        this.setPage(1);
        this.loading = false;
      },
      err => {
        console.error('Error loading sequence children.' + err);
        this.children = [];
      }
    );
  }

  getSequenceChildren(id) {
      this.childrenService.getSequence(id).subscribe(
      res => {
        this.sequence = res.json();
        this.children = res.json().sequenceChildren;
        this.setPage(1);
        this.loading = false;
      },
      err => {
        console.error('Error loading sequence children.' + err);
        this.children = [];
      }
    );
  }

  deleteChild(id) {
    const dialogRef = this.modal.confirm().size('lg').isBlocking(true).showClose(false).okBtn('Sim').cancelBtn('Não')
    .title('Eliminar criança').body(`Tem a certeza que pretende eliminar a criança?`).open();

    dialogRef.then(dialogRef => { dialogRef.result.then(result => {
      if (result) {
        this.childrenService.deleteChild(id).subscribe(
          res => {
            this.childrenService.setSuccess(true);
            this.childrenService.setFailure(false);
            this.childrenService.setTextSuccess('Criança eliminada com sucesso.');
            this.fetchChildren();
          },
          err => {
            console.log('Error deleting child');
            this.childrenService.setSuccess(false);
            this.childrenService.setFailure(true);
            this.childrenService.setTextFailure('Não foi possível eliminar a criança.');
            this.updateSuccessFailure();
          }
        );
      } else {
        this.goBack();
      }
    }).catch(() => {})});
  }

  public truncate(str: String, size: number) {
    if (str != null) {
      let result: String = str;
      if (str.length > size) {
        result = str.substring(0, size + 1) + ' (...)';
      }
      return result;
    } else {
      return '';
    }
  }

  goBack() {
    this.location.back();
  }

  reset() {
    this.childrenService.success = false;
    this.childrenService.failure = false;
    this.childrenService.textFailure = '';
    this.childrenService.textSuccess = '';
  }

  setPage(page: number) {
    if (page < 1 || page > this.pager.totalPages) {
      return;
    }

  // get pager object from service
  this.pager = this.paginationService.getPager(this.children.length, page);

  // get current page of items
  this.pagedItems = this.children.slice(this.pager.startIndex, this.pager.endIndex + 1);
  }
}
