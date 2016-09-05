import { Component, OnInit } from '@angular/core';
import { ChildrenService } from '../services/children.service';


@Component({
    selector: 'children',
    templateUrl: 'app/html/children.html',
    providers: [ChildrenService]
})
export class ChildrenComponent implements OnInit{

    private children = [];
    genders = ['Female', 'Male', 'Other'];
    model = { username: 'Username', 
            password: 'Password',
            confirmpassword: 'Confirm password', 
            firstname: 'First name', 
            lastname: 'Last name' };
    addMode = false;
    active = true;

    constructor(private service: ChildrenService) {
        console.log("Creating children component...")
    }

    ngOnInit() {
        this.getChildren();
    }

    ngOnChanges() {
        this.getChildren();
    }

    getChildren() {
        this.service.getChildren().subscribe(
            result => this.children = result,
            err => console.log('Error loading children: ' + err),
            () => console.log(this.children)
        );
    }
    
    onSubmit() { 
        this.addMode = false; 
    }

    toogleAddMode() {
        this.model = { username: '', password:'', confirmpassword: '', firstname: '', lastname: '' };
        this.active = false;
        setTimeout(() => this.active = true, 0);
        this.addMode = !this.addMode;
    }

}