import { Component, OnInit } from '@angular/core';
import { Resource } from '../../models/resource';
import { ResourcesService } from '../../services/resources/resources.service';
import { ActivatedRoute, Params, Router } from '@angular/Router';
import { Observable } from 'rxjs/Observable';
import { IMyDpOptions, IMyDateModel, IMyDate } from 'mydatepicker';

@Component({
  selector: 'app-add-resource',
  templateUrl: './add-resource.component.html',
  styleUrls: ['./add-resource.component.css']
})



export class AddResourceComponent implements OnInit {

  resource: Resource = new Resource();
  loading = false;
  loadingAdd = false;
  error: string = undefined;

  constructor(public resodurceService: ResourcesService, public router: Router, public route: ActivatedRoute) { }

    ngOnInit() {
        this.loading = true;
        this.loading = false;
    }

    createResource() {

    }

}
