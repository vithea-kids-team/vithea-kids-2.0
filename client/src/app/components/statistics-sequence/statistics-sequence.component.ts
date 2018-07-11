import { Component, Input, OnInit, OnChanges } from '@angular/core';
import { ActivatedRoute, Params }   from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { Sequence } from '../../models/sequence';
import { ChildrenService } from '../../services/children/children.service';
import { Location } from '@angular/common';
import { PaginationService } from '../../services/pagination/pagination.service';
import { Overlay } from 'ngx-modialog';
import { Modal } from 'ngx-modialog/plugins/bootstrap';
import { SequenceLog } from '../../models/sequenceLog';

@Component({
  selector: 'app-statistics-sequence',
  templateUrl: './statistics-sequence.component.html',
  styleUrls: ['./statistics-sequence.component.css']
})
export class StatisticsSequenceComponent implements OnInit, OnChanges {

  @Input() private childInput: any;

  public childId;
  public sequences: Array<Sequence>;
  public sequencesStats: Array<SequenceLog> = new Array(1); // should be changed later
  public loading: boolean = false;


  // pager object
  pager: any = {};

  // paged items
  pagedItems: any = [];


  constructor(public route: ActivatedRoute, public childrenService: ChildrenService, public location: Location,
    public paginationService: PaginationService, public modal: Modal) { }

  ngOnInit() {
    this.childId = this.childInput;

    // change later
    let stats = new SequenceLog;
    stats.name = 'Teste';
    stats.date = '25 May 2018';
    stats.hourInit = '18:16';
    stats.hourEnd = '18:57';
    stats.correctExercices = 10;
    stats.totalExercises = 17;
    stats.percentage = ((stats.correctExercices / stats.totalExercises) * 100).toFixed(2);
    stats.sequenceId = 1;
    this.sequencesStats[0] = stats;

    let stats2 = new SequenceLog;
    stats2.name = 'Comida';
    stats2.date = '28 May 2018';
    stats2.hourInit = '14:47';
    stats2.hourEnd = '16:03';
    stats2.correctExercices = 7;
    stats2.totalExercises = 8;
    stats2.percentage = ((stats2.correctExercices / stats2.totalExercises) * 100).toFixed(2);
    stats2.sequenceId = 2;
    this.sequencesStats[1] = stats2;

    this.getChildSequencesLog(this.childId);
  }

  ngOnChanges() {
    this.childId = this.childInput;
    this.getChildSequencesLog(this.childId);
  }

  public getChildSequencesLog(id) {
    this.childrenService.getChildSequencesLog(id).subscribe(
      result => {
        this.sequencesStats = result;
        console.log(result);
        if (this.sequencesStats.length > 0) {
          this.setPage(1);
        } else {
          this.sequencesStats = [];
          this.pagedItems = [];
        }

        this.loading = false;
      },
      err => {
        console.error('Error loading child log sequences.' + err);
        this.sequencesStats = [];
      }
    );
  }

  setPage(page: number) {
    if (page < 1 || page > this.pager.totalPages) {
      return;
     }

     // get pager object from service
    this.pager = this.paginationService.getPager(this.sequencesStats.length, page);

    // get current page of items
    this.pagedItems = this.sequencesStats.slice(this.pager.startIndex, this.pager.endIndex + 1);
    }

}
