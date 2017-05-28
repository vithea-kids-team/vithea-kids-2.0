import { Component, Input, ViewChild } from '@angular/core';

import { ResourcesService } from '../../services/resources/resources.service';

@Component({
  selector: 'file-upload',
  templateUrl: './file-upload.component.html',
  styleUrls: ['./file-upload.component.css']
})
export class FileUploadComponent {

  @Input() label : string;
  @Input() name : string;
  @Input() resourceType : string;

  @ViewChild('input') input; 

  constructor(private resourcesService : ResourcesService) { }

  toggleFileUpload() {
    this.input.nativeElement.click();
  }

  uploadFile(e) {
    let files = e.target.files;
    if (files && files.length > 0) {
        this.resourcesService.uploadFiles(files, this.resourceType, this.name).subscribe(
          res => this.resourcesService.fetchResources(),
          err => console.error("Error uploading file", err)
        )
    }
  }

}
