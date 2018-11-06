import { Component, OnInit, OnChanges } from '@angular/core';
import { ActivatedRoute, Params }   from '@angular/router';
import { Observable, AnonymousSubject } from 'rxjs/Rx';
import { Child } from '../../models/child';
import { ChildrenService } from '../../services/children/children.service';
import { Location } from '@angular/common';
import { PaginationService } from '../../services/pagination/pagination.service';
import { Overlay } from 'ngx-modialog';
import { Modal } from 'ngx-modialog/plugins/bootstrap';

@Component({
  selector: 'app-settings',
  templateUrl: './statistics.component.html',
  styleUrls: ['./statistics.component.css']
})
export class StatisticsComponent implements OnInit {

  public children: Array<Child>;
  public loading: boolean = false;
  public childId: number;


  public lineChartData: Array<any> = [
    {data: [65, 59, 80, 81, 56, 55, 40, 50, 4, 15, 90, 100], label: 'Series A'},
    {data: [28, 48, 40, 19, 86, 27, 90, 43, 100, 35, 20, 50], label: 'Series B'},
    {data: [18, 48, 77, 9, 100, 27, 40, 12, 40, 67, 11, 45], label: 'Series C'}
  ];
  public lineChartLabels: Array<any> = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'];
  public lineChartOptions: any = {
    responsive: true
  };
  public lineChartColors: Array<any> = [
    { // grey
      backgroundColor: 'rgba(148,159,177,0.2)',
      borderColor: 'rgba(148,159,177,1)',
      pointBackgroundColor: 'rgba(148,159,177,1)',
      pointBorderColor: '#fff',
      pointHoverBackgroundColor: '#fff',
      pointHoverBorderColor: 'rgba(148,159,177,0.8)'
    },
    { // dark grey
      backgroundColor: 'rgba(77,83,96,0.2)',
      borderColor: 'rgba(77,83,96,1)',
      pointBackgroundColor: 'rgba(77,83,96,1)',
      pointBorderColor: '#fff',
      pointHoverBackgroundColor: '#fff',
      pointHoverBorderColor: 'rgba(77,83,96,1)'
    },
    { // grey
      backgroundColor: 'rgba(148,159,177,0.2)',
      borderColor: 'rgba(148,159,177,1)',
      pointBackgroundColor: 'rgba(148,159,177,1)',
      pointBorderColor: '#fff',
      pointHoverBackgroundColor: '#fff',
      pointHoverBorderColor: 'rgba(148,159,177,0.8)'
    }
  ];
  public lineChartLegend: boolean = true;
  public lineChartType: string = 'line';

  constructor(public route: ActivatedRoute, public childrenService: ChildrenService, public location: Location,
    public paginationService: PaginationService, public modal: Modal) { }

  ngOnInit() {
    this.fetchChildren();
  }


  fetchChildren() {
    this.childrenService.getChildren().subscribe(
      result => {
        this.children = result;
        this.loading = false;
      },
      err => {
        console.error('Error loading sequence children.' + err);
        this.children = [];
        this.loading = false;
      }
    );
  }

  updateChild(childId: number) {
    this.childId = childId;
    console.log("updateChild" + this.childId)
  }

  goBack() {
    this.location.back();
  }

}
