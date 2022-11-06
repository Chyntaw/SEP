import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {User} from "../../models/roles/user/user";
import {LoginserviceService} from "../../services/loginservice.service";
import {provideRoutes} from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  user:User = new User();


  constructor(private fb: FormBuilder, private loginserviceService: LoginserviceService) { }

  loginForm!:FormGroup;




  ngOnInit(): void {
    this.loginForm=this.fb.group({email:['',Validators.required],
                                         password:['',Validators.required]})


  }
userLogin() {
  console.log(this.user)
  this.loginserviceService.loginUser(this.user).subscribe(data=>{
   alert("Login ist korrekt")


  },error=>alert("Bitte überprüfen Sie Ihre Eingaben!"));


}


}
