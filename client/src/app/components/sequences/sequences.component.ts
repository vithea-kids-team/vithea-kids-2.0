import 'rxjs/add/operator/switchMap';

import { Component, OnInit, OnChanges } from '@angular/core';
import { ActivatedRoute, Params }   from '@angular/Router';
import { Observable } from 'rxjs/Observable';
import { Router } from '@angular/Router';
import { Location } from '@angular/common';
import { Sequence } from '../../models/sequence';
import { Child } from '../../models/child';
import { ChildrenService } from '../../services/children/children.service';
import { SequencesService } from '../../services/sequences/sequences.service';
import { PaginationService } from '../../services/pagination/pagination.service';
import { Overlay } from 'ngx-modialog';
import { Modal } from 'ngx-modialog/plugins/bootstrap';

@Component({
  selector: 'app-sequences',
  templateUrl: './sequences.component.html',
  styleUrls: ['./sequences.component.css']
})
export class SequencesComponent implements OnInit, OnChanges {

  public childId = 0;
  public sequenceId = 0;
  public sequenceName;
  public child;
  public sequences: Array<Sequence>;
  public loading = false;
  public children: Array<Child>;
  public success = false;
  public failure = false;
  public textSuccess;
  public textFailure;

  // pager object
  pager: any = {};

  // paged items
  pagedItems: any[];

  constructor(public route: ActivatedRoute, public childrenService: ChildrenService, public sequencesService: SequencesService,
    public paginationService: PaginationService, public router: Router, public location: Location, public modal: Modal) { }

  ngOnInit() {
    this.fetchSequences();
  }

  ngOnChanges() {
    this.fetchSequences();
  }

  public fetchSequences() {
     this.loading = true;
     this.updateSuccessFailure();
     this.route.params
      .switchMap((params: Params) => Observable.of(params))
      .subscribe(params => {
        const id: number = parseInt(params['childid'], 10);
        if (id) {
          this.childId = id;
          this.getChild(id);
          this.getChildSequences(id);
        } else {
          this.getSequences();
        }
      }, err => {
        console.error('Error loading sequences', err);
        this.loading = false;
      });
  }

    public getChild(id) {
      this.childrenService.getChild(id).subscribe(
        result => {
          this.child = result;
          this.loading = false;
        },
        err => {
          console.error('Error loading child.' + err);
          this.child = [];
        }
      );
    }

  public getChildSequences(id) {
    this.childrenService.getChildSequences(id).subscribe(
      result => {
        this.sequences = result;

        this.setPage(1);

        this.loading = false;
      },
      err => {
        console.error('Error loading child sequences.' + err);
        this.sequences = [];
      }
    );
  }

  public getSequences() {
    this.sequencesService.getSequences().subscribe(
      result => {
        this.sequences = result;

        this.setPage(1);
        this.loading = false;
      },
      err => {
        console.error('Error loading sequences.' + err);
        this.sequences = [];
      }
    );
  }

  public getSequenceChildren(sequenceId) {
    this.sequencesService.getSequenceChildren(sequenceId).subscribe(
      result => {
        this.children = result;
      },
      err => {
        console.error('Error loading children of sequence.' + err);
        this.children = [];
      }
    );
  }

  public deleteSequence(sequenceId) {

    const dialogRef = this.modal.confirm().size('lg').isBlocking(true).showClose(false).okBtn('Sim').cancelBtn('Não')
      .title('Eliminar aula').body('Tem a certeza que pretende eliminar a aula?').open();

    dialogRef.then(dialogRef => { dialogRef.result.then(result => {
      if (result) {
        this.sequencesService.deleteSequence(sequenceId).subscribe(
          res => {
            this.sequencesService.setSuccess(true);
              this.sequencesService.setFailure(false);
              this.sequencesService.setTextSuccess('Aula eliminada com sucesso.');
              this.fetchSequences();
          },
          err => {
            console.log('Error deleting sequence');
            this.sequencesService.setSuccess(false);
            this.sequencesService.setFailure(true);
            this.sequencesService.setTextFailure('Não foi possível eliminar a aula.');
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

  public updateSuccessFailure() {
    this.success = this.sequencesService.getSuccess();
    this.failure = this.sequencesService.getFailure();
    this.textSuccess = this.sequencesService.getTextSuccess();
    this.textFailure = this.sequencesService.getTextFailure();
  }

  setPage(page: number) {
    if (page < 1 || page > this.pager.totalPages) {
      return;
     }

     // get pager object from service
    this.pager = this.paginationService.getPager(this.sequences.length, page);

    // get current page of items
    this.pagedItems = this.sequences.slice(this.pager.startIndex, this.pager.endIndex + 1);
    }

    reset() {
      this.sequencesService.success = false;
      this.sequencesService.failure = false;
      this.sequencesService.textFailure = '';
      this.sequencesService.textSuccess = '';
    }

}
