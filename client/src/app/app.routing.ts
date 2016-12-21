import { Routes } from '@angular/router'

import { AppComponent } from './components/app.component';
import { ChildrenComponent } from './components/children/children.component';
import { AddChildComponent } from './components/add-child/add-child.component';
import { ExercisesComponent } from './components/exercises/exercises.component';
import { AddExerciseComponent } from './components/add-exercise/add-exercise.component';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';

export const appRoutes: Routes = [
  {
    path: '',
    redirectTo: '/home',
    pathMatch: 'full'
  },
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'home',
    component: HomeComponent
  },
  {
    path: 'children',
    children: [{
      path: '',
      component: ChildrenComponent
    }, 
    {
      path: 'new',
      component: AddChildComponent
    }]
  },
  {
    path: 'exercises',
    children: [{
      path: '',
      component: ExercisesComponent
    }, {
      path: 'new',
      component: AddExerciseComponent
    }]
  }
];