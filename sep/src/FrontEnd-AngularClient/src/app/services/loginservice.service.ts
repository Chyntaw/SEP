import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {User} from "../models/roles/user/user";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class LoginserviceService {

  private databaseURL="http://localhost:8080/user/login"

  constructor(private httpClient: HttpClient) { }

  loginUser(user:User):Observable<any>{
    console.log(user)
    return this.httpClient.post(`${this.databaseURL}`, user);


  }
  loginUser2(eMail:string,password:string):Observable<any>{

    const formData =new FormData();
    formData.append('eMail',eMail)
    formData.append('password',password)
    console.log(formData)
    return this.httpClient.post(`${this.databaseURL}Invite`, formData);


  }












}
