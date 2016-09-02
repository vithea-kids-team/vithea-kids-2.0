import { Component } from '@angular/core';
import { HTTP_PROVIDERS } from '@angular/http'
import { Configuration } from '../config/app.config'
import { TranslateService } from 'ng2-translate/ng2-translate'
@Component({
  selector: 'my-app',
  templateUrl: 'app/html/app.component.html',
  providers: [ HTTP_PROVIDERS, Configuration ]
})
export class AppComponent { 

  constructor(translate : TranslateService) {    
    translate.setDefaultLang('en');
    translate.use('pt');
    console.log(translate.currentLang);
  }
}
