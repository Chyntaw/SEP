import {Component, OnInit} from '@angular/core';
import {User} from "../../../models/roles/user/user";
import {RegistrationserviceService} from "../../../services/registrationservice.service";
import {Router} from "@angular/router";
import {Admin} from "../../../models/roles/admin/admin";
import {error} from "@angular/compiler-cli/src/transformers/util";
//import {Role} from "../../models/role/role";

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {

    pictureFiles?: FileList;
    currentPicture?: File;

    user:User=new User(); //in diesem Objekt die Daten speichern die wir aus der Registrierungsform bekommen

  constructor(private registrationservice: RegistrationserviceService, private dashboardRoute:Router) { }

  selectProfilePicture(event: any): void{
    this.pictureFiles = event.target.files;
  }


  ngOnInit(): void {

  }


  userRegistration(): void{         //MitPB = User und kein Admin
    console.log(this.user);
    if(this.pictureFiles){
      const pictureFile: File | null=this.pictureFiles?.item(0);
      if(pictureFile){

        this.currentPicture = pictureFile;
        this.registrationservice.addUser(this.currentPicture,
          this.user.lastName,
          this.user.firstName,
          this.user.birthDate,
          this.user.eMail,
          this.user.password,
          this.user.role).subscribe(data=>{

          let newUser = (<User>data)

          localStorage.setItem("id", newUser['id']);
          localStorage.setItem("eMail", newUser['eMail']);
          localStorage.setItem("firstName", newUser['firstName']);
          localStorage.setItem("lastName", newUser['lastName']);
          localStorage.setItem("password", newUser['password']);
          localStorage.setItem("role", newUser['role'])


          if(this.user) {
            this.dashboardRoute.navigate(['/dashboard'])
          }

          alert("Registrierung erfolgreich")


        },error1 => alert("Registrierung war nicht erfolgreich"));

      }
    }
    else{
      this.registrationservice.addUserOhnePB(this.user.lastName,
        this.user.firstName,
        this.user.birthDate,
        this.user.eMail,
        this.user.password,
        this.user.role).subscribe(data=>{

        let newUser = (<User>data)

        //localStorage.setItem("0", newUser['id']);
        localStorage.setItem("eMail", newUser['eMail']);
        localStorage.setItem("firstName", newUser['firstName']);
        localStorage.setItem("lastName", newUser['lastName']);
        localStorage.setItem("password", newUser['password']);
        localStorage.setItem("role", newUser['role'])


        if(this.user) {
          this.dashboardRoute.navigate(['/dashboard'])
        }
        else{ this.dashboardRoute.navigate(['/admin-dashboard'])}

      },error=>alert("Registrierung ohne PB war nicht erfolgreich"));
    }
  }
}






