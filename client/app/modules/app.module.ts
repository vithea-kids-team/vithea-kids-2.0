import { NgModule }      from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule }   from '@angular/forms';
import { HttpModule }   from '@angular/http';
import { TranslateModule } from 'ng2-translate/ng2-translate'
import { routing }        from '../app.routing';

import { AppComponent }  from '../components/app.component';
import { ChildrenComponent }  from '../components/children.component';
import { ExercisesComponent }  from '../components/exercises.component';
import { ChildrenService }  from '../services/children.service';
import { ExercisesService }  from '../services/exercises.service';

@NgModule({
  imports: [ 
    BrowserModule, 
    routing, 
    FormsModule,
    HttpModule,
    TranslateModule.forRoot() 
  ],
  declarations: [ AppComponent, ChildrenComponent, ExercisesComponent ],
  providers: [ ChildrenService, ExercisesService],
  bootstrap:    [ AppComponent ]
})
export class AppModule { }
