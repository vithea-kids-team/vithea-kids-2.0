import 'rxjs/add/operator/switchMap';

import { Component, OnInit, OnChanges } from '@angular/core';
import { ActivatedRoute, Params }   from '@angular/Router';
import { Observable } from 'rxjs/Observable';

import { Sequence } from '../../models/sequence';
import { ChildrenService } from '../../services/children/children.service';
import { SequencesService } from '../../services/sequences/sequences.service';
import { PaginationService } from '../../services/pagination/pagination.service';

@Component({
  selector: 'app-sequences',
  templateUrl: './sequences.component.html',
  styleUrls: ['./sequences.component.css']
})
export class SequencesComponent implements OnInit, OnChanges {

  private childId : number;
  private sequences: Array<Sequence>;
  private loading: boolean = false;

  // pager object
  pager: any = {};

  // paged items
  pagedItems: any[];

  constructor(private route: ActivatedRoute, private childrenService : ChildrenService, 
      private sequencesService : SequencesService, private paginationService : PaginationService) { }

  ngOnInit() {
    this.fetchSequences();
  }

  ngOnChanges() {
    this.fetchSequences();
  }

  private fetchSequences() {
     this.loading = true;
     this.route.params
      .switchMap((params: Params) => Observable.of(params))
      .subscribe(params => {
        const id : number = parseInt(params['childid']);
        if(id) {
          this.childId = id;
          this.getChildSequences(id);
        } else {
          this.getSequences();
        }
      }, err => {
        console.error("Error getting sequences", err);
        this.loading = false;
      });
  }

  private getChildSequences(id) {
    this.childrenService.getChildSequences(id).subscribe(
      result => {
        this.sequences = result;

        // initialize to page 1
        this.setPage(1);

        this.loading = false;
      }, 
      err => {
        console.error("Error loading child sequences. " + err);
        this.sequences = [];
      } 
    );
  }


  private getSequences() {
    this.sequencesService.getSequences().subscribe(
      result => {
        this.sequences = result;

        // initialize to page 1
        this.setPage(1);
        this.loading = false;
      }, 
      err => {
        console.error("Error loading sequences. " + err);
        this.sequences = [];
      }
    );
  }

  private deleteSequence(sequenceId) {
    this.loading = true;
    this.sequencesService.deleteSequence(sequenceId).subscribe(
      result => this.fetchSequences(),
      err => {
        console.log("Error deleting sequence");
        this.loading = false;
      }
    )
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
}
