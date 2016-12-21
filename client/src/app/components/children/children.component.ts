import { Component, OnInit, OnDestroy, OnChanges } from '@angular/core';
import { Child } from '../../models/Child';
import { ChildrenService } from '../../services/children/children.service';

@Component({
  selector: 'app-children',
  templateUrl: './children.component.html',
  styleUrls: ['./children.component.css']
})
export class ChildrenComponent implements OnInit, OnChanges, OnDestroy {

  private children: Array<Child> = [];
  private subs = []

  constructor(private service: ChildrenService) { }

  ngOnInit() {
    this.getChildren();
  }

  ngOnChanges() {
    this.getChildren();
  }

  getChildren() {
    this.subs.push(this.service.getChildren().subscribe(
      result => {
        this.children = result
      },
      err => console.log('Error loading children: ' + err)
    ))
  }

  ngOnDestroy() {
    this.subs.forEach(subscription => {
      subscription.unsubscribe()
    });
  }
}
