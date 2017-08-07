import { Component, OnInit, OnChanges } from '@angular/core';
import { Child } from '../../models/child';
import { ChildrenService } from '../../services/children/children.service';
import { Location } from '@angular/common';

@Component({
  selector: 'app-children',
  templateUrl: './children.component.html',
  styleUrls: ['./children.component.css']
})
export class ChildrenComponent implements OnInit, OnChanges {

  public children: Array<Child>;
  public searchBy: string = '';
  public loading:boolean = false;

  constructor(public service: ChildrenService, public location: Location) { }

  ngOnInit() {
    this.getChildren();
  }

  ngOnChanges() {
    this.getChildren();
  }

  getChildren() {
    this.loading = true;
    this.service.getChildren().subscribe(
      result => {
        this.children = result;
        this.loading = false;
      },
      err => {
        console.log('Error loading children: ' + err);
        this.loading = false;
      }
    )
  }

  deleteChild(id) {
    this.loading = true;
    this.service.deleteChild(id).subscribe(
      result => this.getChildren(),
      err => {
        console.log("Error deleting child", id);
        this.loading = false;
      }
    )
  }

  goBack() {
    this.location.back();
  }
}
