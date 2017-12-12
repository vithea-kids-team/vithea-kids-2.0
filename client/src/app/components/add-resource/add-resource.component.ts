import { Component, OnInit, DoCheck} from '@angular/core';
import { Resource } from '../../models/resource';
import { ResourcesService } from '../../services/resources/resources.service';
import { Location } from '@angular/common';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { IMyDpOptions, IMyDateModel, IMyDate } from 'mydatepicker';

@Component({
  selector: 'app-add-resource',
  templateUrl: './add-resource.component.html',
  styleUrls: ['./add-resource.component.css']
})

export class AddResourceComponent implements OnInit, DoCheck {

  public loading = false;
  public loadingAdd = false;
  public stimulusanswersImgs = [];
  public reinforcementImgs = [];
  error: string = undefined;
  public newResource: Resource = new Resource();
  public types = ['Estímulos e Respostas', 'Reforços']

  public uploading = false;
  public textUploading;
  public failure = false;
  public textFailure;

  constructor(public resourcesService: ResourcesService, public router: Router, public route: ActivatedRoute, public location: Location) { }

    ngOnInit() {
      this.reset();
    }

    ngDoCheck() {
      this.updateUploading();
      this.updateFailure();
    }

    public updateUploading() {
      this.uploading = this.resourcesService.getUploading();
      this.textUploading = this.resourcesService.getTextUploading();
    }

    public updateFailure() {
      this.failure = this.resourcesService.getFailure();
      this.textFailure = this.resourcesService.getTextFailure();
    }

    _UploadStimulus_(results, type) {
      this.loading = true;
      const last = results.length - 1;
      const lastItem = results[last];
      lastItem.selected = true;

      this.stimulusanswersImgs.push(Object.assign({}, lastItem));

      this.loading = false;
    }

    _UploadReinforcement_(results, type) {
      this.loading = true;
      const last = results.length - 1;
      const lastItem = results[last];
      lastItem.selected = false;

      this.reinforcementImgs.push(Object.assign({}, lastItem));

      this.loading = false;
    }

    reset() {
      this.resourcesService.uploading = false;
      this.resourcesService.textUploading = '';
    }


    goBack() {
      this.location.back();
    }

}
