import { Component, OnInit } from '@angular/core';
import { CaregiverService } from '../../services/caregiver/caregiver.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  constructor(public caregiverService : CaregiverService) { }

  ngOnInit() {
  }

}
