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
  loading: boolean = false;
  loadingAdd: boolean = false;
  error: string = undefined;
  
  private myDatePickerOptions: IMyDpOptions = {
        dateFormat: 'dd/mm/yyyy'
  };
  
  private selDate: IMyDate;

  constructor(private childService: ChildrenService, private router: Router, private route: ActivatedRoute) { }

  ngOnInit() {
    this.loading = true;
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
              this.loading = false;
            },
            err => console.log("Error getting child")
          );
        } else {
          let d = new Date();
              this.selDate = {year: d.getFullYear(), 
                        month: d.getMonth() + 1, 
                        day: d.getDate()};
              this.model.birthDate = d.toISOString();
           this.loading = false;   
        }
      });
  }

  createChild() {
    this.loadingAdd = true;
    this.error = undefined;
    if (this.model.childId) {
      this.childService.editChild(this.model).subscribe(
        res => this.router.navigate(['/children/']),
        err => {
          this.error = err._body;
          this.loadingAdd = false;
          console.error("Error editing a child. ", err);
        }
      );
    } else {
      this.childService.addChildren(this.model).subscribe(
        res => this.router.navigate(['/children']),
        err => {
          this.error = err._body;
          this.loadingAdd = false;
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
