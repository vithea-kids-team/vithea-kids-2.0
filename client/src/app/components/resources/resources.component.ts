import { Component, OnInit, OnChanges } from '@angular/core';
import { ActivatedRoute, Params }   from '@angular/Router';
import { Location } from '@angular/common';
import { PaginationService } from '../../services/pagination/pagination.service';
import { ResourcesService } from '../../services/resources/resources.service';
import { Overlay } from 'ngx-modialog';
import { Modal } from 'ngx-modialog/plugins/bootstrap';

@Component({
  selector: 'app-resources',
  templateUrl: './resources.component.html',
  styleUrls: ['./resources.component.css']
})
export class ResourcesComponent implements OnInit, OnChanges {

  public resources = [];
  public loading = false;
  public success = false;
  public failure = false;
  public textSuccess;
  public textFailure;


  // pager object
  pager: any = {};

  // paged items
  pagedItems: any[];

  public stimuliFilter = true;
  public reinforcement = true;

  constructor(public route: ActivatedRoute, public resourcesService: ResourcesService, public location: Location,
    public paginationService: PaginationService, public modal: Modal) { }

  ngOnInit() {
    this.fetchResources();
  }

  ngOnChanges() {
    this.fetchResources();
  }

  public updateSuccessFailure() {
    this.success = this.resourcesService.getSuccess();
    this.failure = this.resourcesService.getFailure();
    this.textSuccess = this.resourcesService.getTextSuccess();
    this.textFailure = this.resourcesService.getTextFailure();
  }

  fetchResources() {
    this.loading = true;
    this.updateSuccessFailure();
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
    const dialogRef = this.modal.confirm().size('lg').isBlocking(true).showClose(false).okBtn('Sim').cancelBtn('Não')
    .title('Eliminar recurso multimédia').body(`Tem a certeza que pretende eliminar o recurso multimédia?`).open();

    dialogRef.then(dialogRef => { dialogRef.result.then(result => {
      if (result) {
        this.resourcesService.removeResource(id).subscribe(
          res => {
            this.resourcesService.setSuccess(true);
            this.resourcesService.setFailure(false);
            this.resourcesService.setTextSuccess('Recurso multimédia eliminado com sucesso.');
            this.fetchResources();
          },
          err => {
            console.log('Error deleting resource');
            this.resourcesService.setSuccess(false);
            this.resourcesService.setFailure(true);
            this.resourcesService.setTextFailure('Não foi possível eliminar o recurso multimédia.');
            this.updateSuccessFailure();
          }
        );
      } else {
        this.goBack();
      }
    }).catch(() => {})});
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

  reset() {
    this.resourcesService.success = false;
    this.resourcesService.failure = false;
    this.resourcesService.textFailure = '';
    this.resourcesService.textSuccess = '';
  }

}
