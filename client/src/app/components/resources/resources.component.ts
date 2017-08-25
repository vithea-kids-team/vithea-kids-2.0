import { Component, OnInit, OnChanges } from '@angular/core';
import { ActivatedRoute, Params }   from '@angular/Router';
import { Location } from '@angular/common';
import { PaginationService } from '../../services/pagination/pagination.service';
import { ResourcesService } from '../../services/resources/resources.service';

@Component({
  selector: 'app-resources',
  templateUrl: './resources.component.html',
  styleUrls: ['./resources.component.css']
})
export class ResourcesComponent implements OnInit, OnChanges {

  public resources = [];
  public loading: boolean = false;

  // pager object
  pager: any = {};

  // paged items
  pagedItems: any[];

  public stimuliFilter = true;
  public reinforcement = true;

  constructor(public route: ActivatedRoute, public resourcesService: ResourcesService, public location: Location,
    public paginationService: PaginationService) { }

  ngOnInit() {
    this.fetchResources();
  }

  ngOnChanges() {
    this.fetchResources();
  }

  fetchResources() {
    this.loading = true;
    this.resourcesService.fetchResources().subscribe(
      res => {
        let stimuli = this.resourcesService.getResourcesByType('stimuli');
        let reinforcement = this.resourcesService.getResourcesByType('reinforcement');
        let animatedcharacter = this.resourcesService.getResourcesByType('animatedcharacter');
        this.resources = stimuli.concat(reinforcement, animatedcharacter);

        this.setPage(1);
        this.loading = false;
      },
      err => {
        console.error('Error getting resources.', err);
        this.loading = false;
      }
    )
  }

  deleteResource(id) {
    this.loading = true;
    this.resourcesService.removeResource(id).subscribe(
      result => this.fetchResources(),
      err => {
        console.log('Error deleting resource', id);
        this.loading = false;
      }
    )

  }

  saveChanges() {

  }

  goBack() {
    this.location.back();
  }

  setPage(page: number) {
    if (page < 1 || page > this.pager.totalPages) {
      return;
    }

    // get pager object from service
    this.pager = this.paginationService.getPager(this.resources.length, page);

    // get current page of items
    this.pagedItems = this.resources.slice(this.pager.startIndex, this.pager.endIndex + 1);
  }

}
