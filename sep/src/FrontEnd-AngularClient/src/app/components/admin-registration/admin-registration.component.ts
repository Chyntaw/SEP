import { Component, OnInit } from '@angular/core';
import {Admin} from "../../models/roles/admin/admin";
import {User} from "../../models/roles/user/user";
import {RegistrationserviceService} from "../../services/registrationservice.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-admin-registration',
  templateUrl: './admin-registration.component.html',
  styleUrls: ['./admin-registration.component.css']
})
export class AdminRegistrationComponent implements OnInit {

  admin:User=new Admin();

  constructor(private registrationservice:RegistrationserviceService, private dashboardRoute:Router){ }


  ngOnInit(): void {
  }

  userRegistration(){
    console.log(this.admin);
    this.registrationservice.addUser(this.admin).subscribe(data=>{
      let newUser = (<User>data)


      //localStorage.setItem("0", newUser['id']);
      localStorage.setItem("1", newUser['eMail']);
      localStorage.setItem("2", newUser['firstName']);
      localStorage.setItem("3", newUser['lastName']);
      localStorage.setItem("4", newUser['password']);
      localStorage.setItem("5", newUser['role'])
      this.dashboardRoute.navigate(['/admin-dashboard'])
      alert("Registrierung erfolgreich")

    },error=>alert("Registrierung war nicht erfolgreich"));



  }

}
