import { Component, OnInit} from '@angular/core';
import { Resource } from '../../models/resource';
import { ResourcesService } from '../../services/resources/resources.service';
import { Location } from '@angular/common';
import { ActivatedRoute, Params, Router } from '@angular/Router';
import { Observable } from 'rxjs/Observable';
import { IMyDpOptions, IMyDateModel, IMyDate } from 'mydatepicker';

@Component({
  selector: 'app-add-resource',
  templateUrl: './add-resource.component.html',
  styleUrls: ['./add-resource.component.css']
})

export class AddResourceComponent implements OnInit {

  public loading = false;
  public loadingAdd = false;
  public stimulusanswersImgs = [];
  public reinforcementImgs = [];
  error: string = undefined;
  public newResource: Resource = new Resource();
  public types = ['Estímulos e Respostas', 'Reforços']

  constructor(public resourcesService: ResourcesService, public router: Router, public route: ActivatedRoute, public location: Location) { }

    ngOnInit() {}

    _UploadStimulus_(results, type) {
      this.loading = true;
      const last = results.length - 1;
      const lastItem = results[last];
      lastItem.selected = true;

      this.stimulusanswersImgs.push(Object.assign({}, lastItem));

      this.loading = false;

      this.router.navigate(['/resources']);
    }

    _UploadReinforcement_(results, type) {
      this.loading = true;
      const last = results.length - 1;
      const lastItem = results[last];
      lastItem.selected = false;

      this.reinforcementImgs.push(Object.assign({}, lastItem));

      this.loading = false;

      this.router.navigate(['/resources']);
    }

    goBack() {
      this.location.back();
    }

}
