import { Component } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { CaregiverService } from '../services/caregiver/caregiver.service';
import { EditCaregiverComponent } from '../components/edit-caregiver/edit-caregiver.component';

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
