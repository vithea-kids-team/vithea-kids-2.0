import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule, Http, XHRBackend } from '@angular/http';
import { RouterModule, Routes } from '@angular/router';
import { TranslateModule, TranslateLoader, TranslateStaticLoader } from 'ng2-translate/ng2-translate';
import { SpinnerComponent } from './vendor/angular2-spinner';
import { MyDatePickerModule } from 'mydatepicker';
import { ModalModule } from 'ngx-modialog';
import { BootstrapModalModule } from 'ngx-modialog/plugins/bootstrap';
import { AlertModule } from 'ngx-bootstrap/alert';

import { appRoutes } from './app.routing';

import { AppComponent } from './components/app.component';
import { ImagePickerComponent } from './components/utils/image-picker/image-picker.component';
import { SignUpComponent } from './components/sign-up/sign-up.component';
import { PreferencesComponent } from './components/preferences/preferences.component';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { LoaderComponent } from './components/loader/loader.component';
import { FileUploadComponent } from './components/file-upload/file-upload.component';
import { SettingsComponent } from './components/settings/settings.component';
import { AdminComponent } from './components/admin/admin.component';

import { CaregiverService } from './services/caregiver/caregiver.service';
import { PaginationService } from './services/pagination/pagination.service';
import { GuardService } from './services/guard/guard.service';
import { HttpApiClient } from './services/http/http-api-client.service';

import { EqualValidator } from './directives/equal-validator.directive';

import { ChildrenComponent } from './components/children/children.component';
import { ChildrenService } from './services/children/children.service';
import { AddChildComponent } from './components/add-child/add-child.component';
import { ChildFilter } from './pipes/child-filter';

import { ExercisesComponent } from './components/exercises/exercises.component';
import { ExercisesService } from './services/exercises/exercises.service';
import { AddExerciseComponent } from './components/add-exercise/add-exercise.component';
import { ExerciseFilter } from './pipes/exercise-filter';
import { ExerciseTypeFilter } from './pipes/exercise-type-filter';

import { SequencesComponent } from './components/sequences/sequences.component';
import { SequencesService } from './services/sequences/sequences.service';
import { AddSequenceComponent } from './components/add-sequence/add-sequence.component';
import { SequenceFilter } from './pipes/sequence-filter';

import { ResourcesComponent } from  './components/resources/resources.component';
import { ResourcesService } from './services/resources/resources.service';
import { AddResourceComponent } from './components/add-resource/add-resource.component';

export function translateFactory(https: Http) {
  return  new TranslateStaticLoader(https, './vithea-kids/assets/i18n', '.json');
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
    }),
    MyDatePickerModule,
    AlertModule.forRoot(),
    ModalModule.forRoot(),
    BootstrapModalModule
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
    SignUpComponent,
    SequencesComponent,
    AddSequenceComponent,
    PreferencesComponent,
    ChildFilter,
    SequenceFilter,
    EqualValidator,
    ExerciseFilter,
    ExerciseTypeFilter,
    LoaderComponent,
    SpinnerComponent,
    FileUploadComponent,
    SettingsComponent,
    AdminComponent,
    ResourcesComponent,
    AddResourceComponent
  ],
  providers: [
    GuardService,
    HttpApiClient,
    ChildrenService,
    ExercisesService,
    CaregiverService,
    SequencesService,
    ResourcesService,
    PaginationService,
    ResourcesComponent
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
