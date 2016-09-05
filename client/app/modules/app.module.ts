import { NgModule }      from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule }   from '@angular/forms';
import { HttpModule }   from '@angular/http';
import { TranslateModule } from 'ng2-translate/ng2-translate'
import { AuthHttp } from 'angular2-jwt'
import { routing }        from '../app.routing';

import { AppComponent }  from '../components/app.component'
import { LoginComponent } from '../components/login.component'
import { ChildrenComponent }  from '../components/children.component'
import { ExercisesComponent }  from '../components/exercises.component'
import { ChildrenService }  from '../services/children.service'
import { ExercisesService }  from '../services/exercises.service'
import { CaregiverService } from '../services/caregiver.service'
import { LoggedInGuard } from '../services/logged-in.guard'
import { Configuration } from '../config/app.config'

@NgModule({
  imports: [ 
    BrowserModule, 
    routing, 
    FormsModule,
    HttpModule,
    TranslateModule.forRoot() 
  ],
  declarations: [ AppComponent, LoginComponent, ChildrenComponent, ExercisesComponent ],
  providers: [ LoggedInGuard, CaregiverService, ChildrenService, ExercisesService, Configuration, AuthHttp],
  bootstrap:    [ AppComponent ]
})
export class AppModule { }
