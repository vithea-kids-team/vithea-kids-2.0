import { Component } from '@angular/core';
import { Router } from '@angular/router';

import { CaregiverService } from '../services/caregiver.service';

@Component({
    selector: 'login',
    templateUrl: 'app/html/login.html'
})
export class LoginComponent {
    constructor(private caregiverService: CaregiverService, private router: Router) { }

    login(event, username, password) {
        this.caregiverService.login(username, password).subscribe((result) => {
            if (result) {
                this.router.navigate(['']);
            }
        });
    }

    signup(event) {
        this.router.navigate(['/signup']);
    }    
}