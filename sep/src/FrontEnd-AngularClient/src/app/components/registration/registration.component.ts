import {Component, OnInit} from '@angular/core';
import {User} from "../../models/roles/user/user";
import {RegistrationserviceService} from "../../services/registrationservice.service";
import {Router} from "@angular/router";
import {Admin} from "../../models/roles/admin/admin";
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

  userRegistration(){
    console.log(this.user);
    this.registrationservice.addUser(this.user).subscribe(data=>{
      let newUser = (<User>data)

      //localStorage.setItem("0", newUser['id']);
      localStorage.setItem("1", newUser['eMail']);
      localStorage.setItem("2", newUser['firstName']);
      localStorage.setItem("3", newUser['lastName']);
      localStorage.setItem("4", newUser['password']);
      localStorage.setItem("5", newUser['role'])


      if(this.user) {
        this.dashboardRoute.navigate(['/dashboard'])
      }
      else{ this.dashboardRoute.navigate(['/admin-dashboard'])}

      alert("Registrierung erfolgreich")

    },error=>alert("Registrierung war nicht erfolgreich"));



    }


  }






