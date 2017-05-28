import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params }   from '@angular/Router';
import { Observable } from 'rxjs/Observable';
import { Router } from '@angular/Router';

import { Sequence } from '../../models/Sequence';

import { SequencesService } from '../../services/sequences/sequences.service';

@Component({
  selector: 'app-add-sequence',
  templateUrl: './add-sequence.component.html',
  styleUrls: ['./add-sequence.component.css']
})
export class AddSequenceComponent implements OnInit {

  private newSequence = new Sequence();

  constructor(private route: ActivatedRoute, private sequencesService : SequencesService, private router: Router) { }

  ngOnInit() {
     this.route.params
      .switchMap((params: Params) => Observable.of(params))
      .subscribe(params => {
        const id : number = parseInt(params['childid']);
        if(id) {
          this.newSequence.childId = id;
        }
      });
  }

  registerSequence() {
    this.sequencesService.registerSequence(this.newSequence)
      .subscribe(res => {
         if (this.newSequence.childId) {
          this.router.navigate(['/children/'+ this.newSequence.childId + '/sequences']);
        }
        else {
          this.router.navigate(['/sequences']);
        }
      },
      err => console.log("Error creating sequence."));
  }

}
