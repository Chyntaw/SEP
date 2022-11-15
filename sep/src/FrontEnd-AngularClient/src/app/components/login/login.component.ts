import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {User} from "../../models/roles/user/user";
import {LoginserviceService} from "../../services/loginservice.service";
import {Router} from "@angular/router";
import {provideRoutes} from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  user:User = new User();


  constructor(private fb: FormBuilder, private loginserviceService: LoginserviceService,
              private zwei_faRouter: Router) { }

  loginForm!:FormGroup;




  ngOnInit(): void {
    this.loginForm=this.fb.group({email:['',Validators.required],
                                         password:['',Validators.required]})


  }
userLogin() {
  console.log(this.user)
  this.loginserviceService.loginUser(this.user).subscribe(data=>{
    console.log(data)
   // let email = data['eMail'];
    const data_str = JSON.stringify(data);
    const jsondata = JSON.parse(data_str)

    localStorage.setItem("id", jsondata['id']);
    localStorage.setItem("eMail", jsondata['eMail']);
    localStorage.setItem("firstName", jsondata['firstName']);
    localStorage.setItem("lastName", jsondata['lastName']);
    localStorage.setItem("password", jsondata['password']);
    localStorage.setItem("role", jsondata['role'])
    //localStorage.setItem("6", jsondata['profilePicture']) TODO: PB speichern in localStorage
    this.zwei_faRouter.navigate(['/zwei-fa'])

   alert("Login ist korrekt")


  },error=>alert("Bitte überprüfen Sie Ihre Eingaben!"));


}


}
