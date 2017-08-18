import 'rxjs/add/operator/switchMap';

import { Component, OnInit, OnChanges } from '@angular/core';
import { ActivatedRoute, Params }   from '@angular/Router';
import { Observable } from 'rxjs/Observable';
import { Child } from '../../models/child';
import { ChildrenService } from '../../services/children/children.service';
import { Location } from '@angular/common';
import { PaginationService } from '../../services/pagination/pagination.service';

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

  // pager object
  pager: any = {};

  // paged items
  pagedItems: any[];

  constructor(public route: ActivatedRoute, public childrenService: ChildrenService, public location: Location, public paginationService: PaginationService) { }

  ngOnInit() {
    this.fetchChildren();
  }

  ngOnChanges() {
    this.fetchChildren();
  }

  fetchChildren() {
    this.loading = true;
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
    this.loading = true;
    this.childrenService.deleteChild(id).subscribe(
      result => this.fetchChildren(),
      err => {
        console.log('Error deleting child', id);
        this.loading = false;
      }
    )
  }

  goBack() {
    this.location.back();
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
