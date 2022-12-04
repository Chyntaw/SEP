import { Component, OnInit } from '@angular/core';
import {Admin} from "../../../models/roles/admin/admin";
import {User} from "../../../models/roles/user/user";
import {RegistrationserviceService} from "../../../services/registrationservice.service";
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

  userRegistration(): void{
    console.log(this.admin);
    this.registrationservice.addAdmin(this.admin.lastName,
      this.admin.firstName,
      this.admin.eMail,
      this.admin.password,
      this.admin.role).subscribe(data=>{

      let newUser = (<User>data)

      sessionStorage.setItem("id", newUser['id']);
      sessionStorage.setItem("eMail", newUser['eMail']);
      sessionStorage.setItem("firstName", newUser['firstName']);
      sessionStorage.setItem("lastName", newUser['lastName']);
      sessionStorage.setItem("password", newUser['password']);
      sessionStorage.setItem("role", newUser['role'])

      this.dashboardRoute.navigate(['/admin-dashboard'])

      alert("Registrierung als Admin erfolgreich")

    }, error => alert("Registrierung als Admin nicht erfolgreich"));
  }

}
