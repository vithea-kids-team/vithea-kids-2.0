import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/Router';
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

  constructor(private childService: ChildrenService, private router: Router) { }

  createChild() {
    this.childService.addChildren(this.model).subscribe(
      res => this.router.navigate(['/children']),
      err => {
        console.error("Error registering new child. "+ err);
      }
    );
  }

  reset() {
    this.model = new Child()
  }

}
