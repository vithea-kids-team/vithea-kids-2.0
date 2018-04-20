import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule, Http, XHRBackend } from '@angular/http';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { RouterModule, Routes } from '@angular/router';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { SpinnerComponent } from './vendor/angular2-spinner';
import { MyDatePickerModule } from 'mydatepicker';
import { ModalModule } from 'ngx-modialog';
import { BootstrapModalModule } from 'ngx-modialog/plugins/bootstrap';
import { AlertModule } from 'ngx-bootstrap/alert';

import { appRoutes } from './app.routing';

import { AppComponent } from './components/app.component';
import { ImagePickerComponent } from './components/utils/image-picker/image-picker.component';
import { SignUpComponent } from './components/sign-up/sign-up.component';
import { EditCaregiverComponent } from './components/edit-caregiver/edit-caregiver.component';
import { PreferencesComponent } from './components/preferences/preferences.component';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { LoaderComponent } from './components/loader/loader.component';
import { FileUploadComponent } from './components/file-upload/file-upload.component';
import { AdminComponent } from './components/admin/admin.component';
import { SettingsComponent } from './components/settings/settings.component';

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
import { UtilsExercisesService} from './services/utils/utils-exercises.service';

import { AddExerciseComponent } from './components/add-exercise/add-exercise.component';
import { AddExerciseMultipleChoiceImageComponent } from './components/multiple-choice-exercise/add-exercise-mc-image/add-exercise-mc-image.component';
import { AddExerciseMultipleChoiceTextComponent } from './components/multiple-choice-exercise/add-exercise-mc-text/add-exercise-mc-text.component';
import { AddExerciseSelectionImageComponent} from './components/selection-image-exercise/add-exercise-selectionImage/add-exercise-selectionImage.component';
import { AddExerciseSpeechComponent} from './components/speech-exercise/add-exercise-speech/add-exercise-speech.component';

import { EditExerciseComponent } from './components/edit-exercise/edit-exercise.component';
import { EditExerciseMultipleChoiceImageComponent } from './components/multiple-choice-exercise/edit-exercise-mc-image/edit-exercise-mc-image.component';
import { EditExerciseMultipleChoiceTextComponent } from './components/multiple-choice-exercise/edit-exercise-mc-text/edit-exercise-mc-text.component';
import { EditExerciseSpeechComponent } from './components/speech-exercise/edit-exercise-speech/edit-exercise-speech.component';

import { ShowExerciseMultipleChoiceImageComponent } from './components/multiple-choice-exercise/show-exercise-mc-image/show-exercise-mc-image.component';
import { ShowExerciseMultipleChoiceTextComponent } from './components/multiple-choice-exercise/show-exercise-mc-text/show-exercise-mc-text.component';
import { ShowExerciseSpeechComponent} from './components/speech-exercise/show-exercise-speech/show-exercise-speech.component';

import { ExerciseFilter } from './pipes/exercise-filter';
import { ExerciseTypeFilter } from './pipes/exercise-type-filter';

import { SequencesComponent } from './components/sequences/sequences.component';
import { SequencesService } from './services/sequences/sequences.service';
import { AddSequenceComponent } from './components/add-sequence/add-sequence.component';
import { SequenceFilter } from './pipes/sequence-filter';

import { ResourcesComponent } from  './components/resources/resources.component';
import { ResourcesService } from './services/resources/resources.service';

import { RecoverPasswordComponent } from  './components/recover-password/recover-password.component';

import { AddResourceComponent } from './components/add-resource/add-resource.component';
import { ResourceTypeFilter } from './pipes/resources-type-filter';

export function translateFactory(https: HttpClient) {
  return  new TranslateHttpLoader(https, './vithea-kids/assets/i18n/', '.json');
}

@NgModule({
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    HttpClientModule,
    RouterModule.forRoot(appRoutes),
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: translateFactory,
        deps: [HttpClient]
      }
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
    AddExerciseMultipleChoiceImageComponent,
    AddExerciseMultipleChoiceTextComponent,
    AddExerciseSelectionImageComponent,
    AddExerciseSpeechComponent,
    EditExerciseComponent,
    EditExerciseMultipleChoiceTextComponent,
    EditExerciseMultipleChoiceImageComponent,
    EditExerciseSpeechComponent,
    ShowExerciseMultipleChoiceTextComponent,
    ShowExerciseMultipleChoiceImageComponent,
    ShowExerciseSpeechComponent,
    ImagePickerComponent,
    SignUpComponent,
    EditCaregiverComponent,
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
    AdminComponent,
    SettingsComponent,
    ResourcesComponent,
    ResourceTypeFilter,
    RecoverPasswordComponent,
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
    ResourcesComponent,
    UtilsExercisesService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
