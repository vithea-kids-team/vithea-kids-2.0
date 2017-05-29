import { Component, OnInit } from '@angular/core';
import { Child } from '../../models/Child';
import { ChildrenService } from '../../services/children/children.service';
import { ActivatedRoute, Params, Router } from '@angular/Router';
import { Observable } from 'rxjs/Observable';
import { IMyDpOptions, IMyDateModel, IMyDate } from 'mydatepicker';

@Component({
  selector: 'app-add-child',
  templateUrl: './add-child.component.html',
  styleUrls: ['./add-child.component.css']
})
export class AddChildComponent implements OnInit {

  genders = ['Female', 'Male', 'Other'];
  model: Child = new Child();
  
  private myDatePickerOptions: IMyDpOptions = {
        dateFormat: 'dd/mm/yyyy'
  };
  
  private selDate: IMyDate = {year: 0, month: 0, day: 0};

  constructor(private childService: ChildrenService, private router: Router, private route: ActivatedRoute) { }

  ngOnInit() {
     this.route.params
      .switchMap((params: Params) => Observable.of(params))
      .subscribe(params => {
        const id : number = parseInt(params['childid']);
        if(id) {
          this.childService.getChild(id).subscribe(
            res => {
              let d = new Date(res.birthDate);
              this.selDate = {year: d.getFullYear(), 
                        month: d.getMonth() + 1, 
                        day: d.getDate()};       
              this.model = res;
            },
            err => console.log("Error getting child")
          );
        }
      });
  }

  createChild() {
    if (this.model.childId) {
      this.childService.editChild(this.model).subscribe(
        res => this.router.navigate(['/children/']),
        err => {
          console.error("Error editing a child. ", err);
        }
      );
    } else {
      this.childService.addChildren(this.model).subscribe(
        res => this.router.navigate(['/children/new/'+ res.json().childId + '/preferences']),
        err => {
          console.error("Error registering new child. ", err);
        });
    }
  }

  onDateChanged(event: IMyDateModel) {
    if (event.jsdate) {
      this.model.birthDate = event.jsdate.toISOString();
    }
  }
}
