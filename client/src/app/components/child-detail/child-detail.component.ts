import 'rxjs/add/operator/switchMap';

import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params }   from '@angular/Router';
import { Observable } from 'rxjs/Observable';

import { ChildrenService } from '../../services/children/children.service';

@Component({
  selector: 'app-child-detail',
  templateUrl: './child-detail.component.html',
  styleUrls: ['./child-detail.component.css']
})
export class ChildDetailComponent implements OnInit {

  child : any;

  constructor(private route: ActivatedRoute, private childrenService : ChildrenService) { }

  ngOnInit() {
    this.route.params
      .switchMap((params: Params) => Observable.of(+params['id']))
      .subscribe(id => this.child = this.childrenService.getChild(id));
  }
}
