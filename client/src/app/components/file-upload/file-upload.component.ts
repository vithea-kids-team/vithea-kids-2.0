import { Component, Input, Output, ViewChild, EventEmitter } from '@angular/core';
import { ResourcesService } from '../../services/resources/resources.service';

@Component({
  selector: 'file-upload',
  templateUrl: './file-upload.component.html',
  styleUrls: ['./file-upload.component.css']

})
export class FileUploadComponent {

  @Input() label: string;
  @Input() name: string;
  @Input() resourceType: string;
  @Output() results = new EventEmitter<any>();

  @ViewChild('input') input;

  constructor(public resourcesService: ResourcesService) { }

  toggleFileUpload() {
    this.input.nativeElement.click();
  }

  uploadFile(e) {
    let files = e.target.files;

    if (files && files.length > 0) {
      this.resourcesService.uploadFiles(files, this.resourceType, this.name).subscribe(
          res => {
            this.resourcesService.fetchResources().subscribe(
              resResources => {
                this.resourcesService.setSuccess(true);
                this.resourcesService.setFailure(false);
                this.resourcesService.setTextSuccess('Recurso multimédia adicionado com sucesso.');
                this.results.emit(this.resourcesService.getResourcesByType(this.resourceType));
              }
            )
          },
          err => {
            this.resourcesService.setSuccess(false);
            this.resourcesService.setFailure(true);
            this.resourcesService.setTextSuccess('Não foi possível adicionar o recurso multimédia.');
            console.error('Error uploading file', err)
        }
        )
    }
  }
}
