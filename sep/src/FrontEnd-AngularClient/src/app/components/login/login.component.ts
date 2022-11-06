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
    this.zwei_faRouter.navigate(['/zwei-fa'])
   alert("Login ist korrekt")


  },error=>alert("Bitte überprüfen Sie Ihre Eingaben!"));


}


}
