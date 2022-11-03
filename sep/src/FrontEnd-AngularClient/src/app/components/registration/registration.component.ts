import { Component, OnInit } from '@angular/core';
import {User} from "../../models/user";
import {RegistrationserviceService} from "../../services/registrationservice.service";

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {

    user:User=new User(); //in diesem Objekt die Daten speichern die wir aus der Registrierungsform bekommen

  constructor(private registrationservice: RegistrationserviceService) { }

  ngOnInit(): void {
  }

  userRegistration(){
    console.log(this.user);
    this.registrationservice.addUser(this.user).subscribe(data=>{

      alert("Registrierung erfolgreich")

    },error=>alert("Registrierung war nicht erfolgreich"));



  }





}
