import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params, Router }   from '@angular/Router';
import { Observable } from 'rxjs/Observable';

import { ChildrenService } from '../../services/children/children.service';

@Component({
  selector: 'app-add-preferences',
  templateUrl: './add-preferences.component.html',
  styleUrls: ['./add-preferences.component.css']
})
export class AddPreferencesComponent implements OnInit {

  private childId : number;
  private greetingMessage: string;
  private exerciseReinforcementMessage: string;
  private sequenceReinforcementMessage: string;

  constructor(private route: ActivatedRoute, private childService: ChildrenService, private router: Router) { }

  ngOnInit() {
     this.route.params
      .switchMap((params: Params) => Observable.of(params))
      .subscribe(params => {
        const id : number = parseInt(params['childid']);
        if(id) {
          this.childId = id;
        }
      });
  }

  setPersonalMessages() {
    this.childService.setPersonalMessages(this.childId, this.greetingMessage, this.exerciseReinforcementMessage, this.sequenceReinforcementMessage)
      .subscribe(res => {
        if (this.route.toString().indexOf('new')) {
          this.router.navigate(['/children']);
        } else {
          this.router.navigate([this.route.toString().replace('/edit', '')])
        }
      },
      err => console.log("Error setting preferences."))
  }

}
