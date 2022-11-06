import {Component, OnInit} from '@angular/core';
import {User} from "../../models/roles/user/user";
import {RegistrationserviceService} from "../../services/registrationservice.service";
import {Router} from "@angular/router";
//import {Role} from "../../models/role/role";

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {

    user:User=new User(); //in diesem Objekt die Daten speichern die wir aus der Registrierungsform bekommen



  constructor(private registrationservice: RegistrationserviceService, private dashboardRoute:Router) { }

  ngOnInit(): void {

  }
  addRole(){
    this.user.role='BASIC';

  }
  userRegistration(){
    console.log(this.user);
    this.registrationservice.addUser(this.user).subscribe(data=>{

      this.dashboardRoute.navigate(['/dashboard'])
      alert("Registrierung erfolgreich")

    },error=>alert("Registrierung war nicht erfolgreich"));



  }





}
