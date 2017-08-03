import { Component } from '@angular/core';
import { TranslateService } from 'ng2-translate/ng2-translate';
import { CaregiverService } from '../services/caregiver/caregiver.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  constructor(translate: TranslateService, public caregiverService: CaregiverService) {
    translate.setDefaultLang('en');
    translate.use('pt');
  }
}
