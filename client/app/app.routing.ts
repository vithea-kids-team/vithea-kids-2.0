import { Routes, RouterModule } from '@angular/router';

import { ChildrenComponent } from './components/children.component';
import { ExercisesComponent } from './components/exercises.component';

const appRoutes: Routes = [
  {
    path: '',
    redirectTo: '/children',
    pathMatch: 'full'
  },
  {
    path: 'children',
    component: ChildrenComponent
  },
  {
    path: 'exercises',
    component: ExercisesComponent
  }
];

export const routing = RouterModule.forRoot(appRoutes);