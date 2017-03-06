import { Component, OnInit } from '@angular/core';
import { Child } from '../../models/Child';
import { ChildrenService } from '../../services/children/children.service';

@Component({
  selector: 'app-add-child',
  templateUrl: './add-child.component.html',
  styleUrls: ['./add-child.component.css']
})
export class AddChildComponent {

  genders = ['Female', 'Male', 'Other'];

  model: Child = new Child()

  constructor(private childService: ChildrenService) { }

  createChild() {
    this.childService.addChildren(this.model);
  }

  reset() {
    this.model = new Child()
  }

}
