import { Component, OnInit, OnChanges } from '@angular/core';
import { Child } from '../../models/child';
import { ChildrenService } from '../../services/children/children.service';

@Component({
  selector: 'app-children',
  templateUrl: './children.component.html',
  styleUrls: ['./children.component.css']
})
export class ChildrenComponent implements OnInit, OnChanges {

  private children: Array<Child>;
  private searchBy: string = '';
  private loading:boolean = false;

  constructor(private service: ChildrenService) { }

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
}
