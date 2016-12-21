import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule, Http } from '@angular/http';
import { RouterModule, Routes } from '@angular/Router'
import { TranslateModule, TranslateLoader, TranslateStaticLoader } from 'ng2-translate/ng2-translate'

import { AppComponent } from './components/app.component';
import { ChildrenComponent } from './components/children/children.component';
import { ExercisesComponent } from './components/exercises/exercises.component';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';

import { ChildrenService } from './services/children/children.service';
import { ExercisesService } from './services/exercises/exercises.service';
import { CaregiverService } from './services/caregiver/caregiver.service';
import { ResourcesService } from './services/resources/resources.service';
import { HttpApiClient } from './services/http/http-api-client.service';

import { appRoutes } from './app.routing';
import { AddChildComponent } from './components/add-child/add-child.component';
import { AddExerciseComponent } from './components/add-exercise/add-exercise.component';
import { ImagePickerComponent } from './components/utils/image-picker/image-picker.component';

@NgModule({
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    RouterModule.forRoot(appRoutes),
    TranslateModule.forRoot({
      provide: TranslateLoader,
      useFactory: (http: Http) => new TranslateStaticLoader(http, '/i18n', '.json'),
      deps: [Http]
    })
  ],
  declarations: [
    AppComponent,
    ChildrenComponent,
    ExercisesComponent,
    HomeComponent,
    LoginComponent,
    AddChildComponent,
    AddExerciseComponent,
    ImagePickerComponent
  ],
  providers: [
    ChildrenService,
    ExercisesService,
    CaregiverService,
    ResourcesService,
    HttpApiClient
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
