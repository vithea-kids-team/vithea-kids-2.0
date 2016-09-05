import { Routes, RouterModule } from '@angular/router'

import { ChildrenComponent } from './components/children.component'
import { ExercisesComponent } from './components/exercises.component'
import { LoginComponent } from './components/login.component'
import { HomeComponent } from './components/home.component'
import { LoggedInGuard } from './services/logged-in.guard'

const appRoutes: Routes = [
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
    component: HomeComponent,
    canActivate: [LoggedInGuard]
  },
  {
    path: 'children',
    component: ChildrenComponent,
    canActivate: [LoggedInGuard]
  },
  {
    path: 'exercises',
    component: ExercisesComponent,
    canActivate: [LoggedInGuard]
  }
];

export const routing = RouterModule.forRoot(appRoutes);