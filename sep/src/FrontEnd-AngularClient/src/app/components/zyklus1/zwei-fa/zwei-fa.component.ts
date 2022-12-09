import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {User} from "../../../models/roles/user/user";
import {ZweiFaserviceService} from "../../../services/zwei-faservice.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-zwei-fa',
  templateUrl: './zwei-fa.component.html',
  styleUrls: ['./zwei-fa.component.css']
})



export class ZweiFaComponent implements OnInit {

  user:User = new User();
  zwei_faForm!:FormGroup;

  constructor(private fb: FormBuilder, private zweiFaserviceService:ZweiFaserviceService,
              private dashboardRouter:Router) { }

  ngOnInit(): void {
  }

  checkCode() {
    console.log(this.user);
    let user_eMail = sessionStorage.getItem("eMail")
    let user_role = sessionStorage.getItem("role")

    // set email in current user and remove "" in first and last position
    this.user.eMail = JSON.stringify(user_eMail).slice(1, -1)

    this.zweiFaserviceService.zweiFaUser(this.user).subscribe(data => {
      if(user_role == "ADMIN") {
        this.dashboardRouter.navigate(['/admin-dashboard'])
      }
      else {
        this.dashboardRouter.navigate(['/dashboard'])
      }
    }, error => alert("Code nicht korrekt"));
  }

}
