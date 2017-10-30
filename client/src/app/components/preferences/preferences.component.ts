import { Component, OnInit, OnChanges } from '@angular/core';
import { ActivatedRoute, Params }   from '@angular/Router';
import { Observable } from 'rxjs/Observable';
import { Location } from '@angular/common';
import { ChildrenService } from '../../services/children/children.service';
import { ResourcesService } from '../../services/resources/resources.service';
import { Overlay } from 'ngx-modialog';
import { Modal } from 'ngx-modialog/plugins/bootstrap';
import { Router } from '@angular/Router';
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

  public reinforcementImageError;

  constructor(public modal: Modal, public route: ActivatedRoute, public childService: ChildrenService,
    public location: Location, public resourcesService: ResourcesService, public router: Router) { }

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

  validatePrompting() {
    if (this.prefs.promptingStrategy !== 'OFF') {
      return true;
    } else {
      return false;
    }
  }

  validateReinforcement() {
    if (this.prefs.reinforcementStrategy !== 'OFF') {
      return true;
    } else {
      return false;
    }
  }

  validateReinforcementImagesAdded() {
    if (this.validateReinforcement) {
      const reinforcementImage = this.reinforcementResources.filter((reinforcementImage) => { return reinforcementImage.selected; });
      if (reinforcementImage.length === 0) {
        this.reinforcementImageError = true;
      } else {
        this.reinforcementImageError = false;
      }
    } else {
      this.reinforcementImageError = false;
    }
}

  getChildPreferences(id: number) {
     this.childService.getChild(id).subscribe(
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
        this.prefs.promptingRead = result.prompting.promptingRead;
        this.prefs.sequenceExercisesOrder = result.sequenceExercisesPreferences.sequenceExercisesOrder;
        this.prefs.sequenceExercisesCapitalization = result.sequenceExercisesPreferences.sequenceExercisesCapitalization;
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

    this.validateReinforcementImagesAdded();

    this.reinforcementImageError = false;

    if (this.reinforcementImageError === false) {

      const dialogRef = this.modal.confirm().size('lg').isBlocking(true).showClose(false).okBtn('Sim').cancelBtn('Não')
      .title('Editar prefererências').body('Tem a certeza que pretende editar as preferências da criança ' + this.childName + '?').open();

      dialogRef.then(dialogRef => { dialogRef.result.then(result => {
        if (result) {
          const animchar = this.animatedCharactersResources.find((res) => { return res.selected });
          this.prefs.animatedCharacterResourceId = animchar ? animchar.resourceId : this.prefs.animatedCharacterResourceId;

          const reinf = this.reinforcementResources.find((res) => { return res.selected });
          this.prefs.reinforcementResourceId = reinf ? reinf.resourceId : this.prefs.reinforcementResourceId;

          this.childService.updatePreferences(this.childId, this.prefs).subscribe(
            res => {
              this.childService.setSuccess(true);
              this.childService.setFailure(false);
              this.childService.setTextSuccess('Preferências da criança ' + this.childName + ' editadas com sucesso.');
              this.getChildPreferences(this.childId);
              this.router.navigate(['/children/']);
            },
            err => {
              console.log('Error setting preferences.');
              this.childService.setSuccess(false);
              this.childService.setFailure(true);
              this.childService.setTextFailure('Não foi possível editar as preferências da criança ' + this.childName + '.');
              this.router.navigate(['/children/']);
            }
          );
        } else {
          this.goBack();
        }
      }).catch(() => {})});
    }
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

  updatePromptingRead(e) {
    this.prefs.promptingRead = e.target.checked;
  }

  goBack() {
    this.location.back();
  }

}
