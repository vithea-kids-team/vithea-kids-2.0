import { Component, OnInit, OnChanges } from '@angular/core';
import { ActivatedRoute, Params }   from '@angular/Router';
import { Observable } from 'rxjs/Observable';
import { ChildrenService } from '../../services/children/children.service';
import { ResourcesService } from '../../services/resources/resources.service';

import { Child } from '../../models/child';
import { Preferences } from '../../models/preferences';

@Component({
  selector: 'app-preferences',
  templateUrl: './preferences.component.html',
  styleUrls: ['./preferences.component.css']
})
export class PreferencesComponent implements OnInit {

  public prefs: Preferences;
  public childId: number;
  public childName: string;

  public animatedCharactersResources;
  public reinforcementResources;

  public loading = false;

  constructor(public route: ActivatedRoute, public childrenService: ChildrenService, public resourcesService: ResourcesService) { }

  ngOnInit() {
    this.loading = true;
    this.route.params
    .switchMap((params: Params) => Observable.of(params))
    .subscribe(params => {
      const id: number = parseInt(params['childid'], 10);
      if (id) {
        this.childId = id;
        this.getChildPreferences(id);
      } else {
        this.loading = false;
      }
    });
  }

  getChildPreferences(id: number) {
     this.childrenService.getChild(id).subscribe(
      result => {
        this.prefs = new Preferences();
        this.prefs.greetingMessage = result.personalMessagesList.find((msg) => {
            return msg.messageType === 'GREETING_MESSAGE';
          }).message;
        this.prefs.exerciseReinforcementMessage = result.personalMessagesList.find((msg) => {
            return msg.messageType === 'EXERCISE_REINFORCEMENT';
          }).message;
        this.prefs.sequenceReinforcementMessage = result.personalMessagesList.find((msg) => {
            return msg.messageType === 'SEQUENCE_REINFORCEMENT';
          }).message;
        if (result.animatedCharacter) {
          this.prefs.animatedCharacterResourceId = result.animatedCharacter.resourceId;
          this.prefs.animatedCharacterResourcePath = result.animatedCharacter.resourcePath;
        }
        this.prefs.promptingStrategy = result.prompting.promptingStrategy;
        this.prefs.promptingColor = result.prompting.promptingColor;
        this.prefs.promptingSize = result.prompting.promptingSize;
        this.prefs.promptingScratch = result.prompting.promptingScratch;
        this.prefs.promptingHide = result.prompting.promptingHide;
        this.prefs.reinforcementStrategy = result.reinforcement.reinforcementStrategy;
        const reinf = result.reinforcement.reinforcementResource;
        if (reinf) {
          this.prefs.reinforcementResourceId = reinf.resourceId;
        this.prefs.reinforcementResourcePath = reinf.resourcePath;
        }
        this.prefs.emotions = result.emotions;

        this.reinforcementResources = this.resourcesService.getResourcesByTypeSelected('reinforcement', this.prefs.reinforcementResourceId);
        this.animatedCharactersResources = this.resourcesService.getResourcesByTypeSelected('animatedcharacter', this.prefs.animatedCharacterResourceId);
        this.childName = result.firstName + ' ' + result.lastName;

        this.loading = false;
      },
      err => {
        this.loading = false;
        console.error('Error loading child sequences.' + err);
      }
    );
  }

  updatePreferences() {
    this.loading = true;
    const animchar = this.animatedCharactersResources.find((res) => {
      return res.selected
    });
    this.prefs.animatedCharacterResourceId = animchar ? animchar.resourceId : this.prefs.animatedCharacterResourceId;

    const reinf = this.reinforcementResources.find((res) => {
      return res.selected
    });
    this.prefs.reinforcementResourceId = reinf ? reinf.resourceId : this.prefs.reinforcementResourceId;

    this.childrenService.updatePreferences(this.childId, this.prefs)
      .subscribe(res => this.getChildPreferences(this.childId),
      err => {
        console.log('Error setting preferences.');
        this.loading = false;
      })
  }

  updateReinforcement(results) {
    const last = results.length - 1;
    const lastItem = results[last];
    lastItem.selected = false;
    this.reinforcementResources.push(Object.assign({}, lastItem));
  }

  updatePromptingSize(e) {
    this.prefs.promptingSize = e.target.checked;
  }

  updatePromptingColor(e) {
    this.prefs.promptingColor = e.target.checked;
  }

  updatePromptingScratch(e) {
    this.prefs.promptingScratch = e.target.checked;
  }

  updatePromptingHide(e) {
    this.prefs.promptingHide = e.target.checked;
  }
}
