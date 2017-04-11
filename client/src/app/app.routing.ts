import { Routes } from '@angular/Router'

import { AppComponent } from './components/app.component';
import { ChildrenComponent } from './components/children/children.component';
import { AddChildComponent } from './components/add-child/add-child.component';
import { SequencesComponent } from './components/sequences/sequences.component';
import { ExercisesComponent } from './components/exercises/exercises.component';
import { AddExerciseComponent } from './components/add-exercise/add-exercise.component';
import { PreferencesComponent } from './components/preferences/preferences.component';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { SignUpComponent } from './components/sign-up/sign-up.component';

import { GuardService } from './services/guard/guard.service';

export const appName = 'vithea-kids-admin';

export const appRoutes: Routes = [
  {
    path: '',
    redirectTo: 'home',
    pathMatch: 'full'
  },
  {
    path: 'home',
    component: HomeComponent
  },
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'signup',
    component: SignUpComponent
  },
  {
    path: 'children',
    children: [{
      path: '',
      component: ChildrenComponent,
      canActivate: [
        GuardService
      ]
      }, 
      {
        path: 'new',
        component: AddChildComponent,
        canActivate: [
          GuardService
        ]
      },
      {
        path: ':id/sequences',
        component: SequencesComponent,
        canActivate: [
          GuardService
        ]
      },
      {
        path: ':id/preferences',
        component: PreferencesComponent,
        canActivate: [
          GuardService
        ]
      }
    ]
  },

  {
    path: 'exercises',
    children: [{
      path: '',
      component: ExercisesComponent,
      canActivate: [
        GuardService
      ]}, 
      {
        path: 'new',
        component: AddExerciseComponent,
        canActivate: [
          GuardService
        ]},
      {
        path: 'new/:id',
        component: AddExerciseComponent,
        canActivate: [
          GuardService
        ]},
      {
        path: ':id',
        //component: ExerciseDetailComponent,
        canActivate: [
          GuardService
        ]
      }]
  }, 
  
  {
    path: 'sequences',
    children: [{
      path: '',
      component: SequencesComponent,
      canActivate: [
        GuardService
      ]
      }, 
      {
        path: 'new',
        //component: AddSequenceComponent,
        canActivate: [
          GuardService
        ]
      },
      {
        path: ':id',
        component: ExercisesComponent,
        canActivate: [
          GuardService
        ]
      }]
    }];