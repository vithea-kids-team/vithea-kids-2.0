import { Component, OnInit } from '@angular/core';
import { ResourcesService } from '../../services/resources/resources.service';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {

  private avatarName: string = '';
  private animatedCharactersResources;

  constructor(private resourcesService : ResourcesService) { }

  ngOnInit() {
    this.resourcesService.fetchAnimatedCharacters().subscribe(
      res => {
        this.animatedCharactersResources = this.resourcesService.getResourcesByType('animatedcharacter');
      }
    )
  }

  updateAnimatedCharacters(results) {
    const last = results.length - 1;
    const lastItem = results[last];
    lastItem.selected = false;
    this.animatedCharactersResources.push(Object.assign({}, lastItem));
  }

}
