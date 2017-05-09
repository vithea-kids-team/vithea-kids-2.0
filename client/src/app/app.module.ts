import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule, Http, XHRBackend } from '@angular/http';
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
import { SequencesService } from './services/sequences/sequences.service';
import { ResourcesService } from './services/resources/resources.service';
import { HttpApiClient } from './services/http/http-api-client.service';
import { GuardService } from './services/guard/guard.service';
import { PaginationService } from './services/pagination/pagination.service';

import { appRoutes } from './app.routing';
import { AddChildComponent } from './components/add-child/add-child.component';
import { AddExerciseComponent } from './components/add-exercise/add-exercise.component';
import { ImagePickerComponent } from './components/utils/image-picker/image-picker.component';
import { TesteInputComponent } from './components/teste-input/teste-input.component';
import { SignUpComponent } from './components/sign-up/sign-up.component';
import { SequencesComponent } from './components/sequences/sequences.component';
import { AddSequenceComponent } from './components/add-sequence/add-sequence.component';
import { PreferencesComponent } from './components/preferences/preferences.component';
import { AddPreferencesComponent } from './components/add-preferences/add-preferences.component';

import { ChildFilter } from './pipes/child-filter';

export function translateFactory(http : Http) {
  return  new TranslateStaticLoader(http, './i18n', '.json');
}

@NgModule({
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    RouterModule.forRoot(appRoutes),
    TranslateModule.forRoot({
      provide: TranslateLoader,
      useFactory: translateFactory,
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
    ImagePickerComponent,
    TesteInputComponent,
    SignUpComponent,
    SequencesComponent,
    AddSequenceComponent,
    PreferencesComponent,
    AddPreferencesComponent,

    ChildFilter
  ],
  providers: [
    GuardService,    
    HttpApiClient,
    ChildrenService,
    ExercisesService,
    CaregiverService,
    SequencesService,
    ResourcesService,
    PaginationService  
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
