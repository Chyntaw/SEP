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

  loginUser(user:User):Observable<object>{
    console.log(user)
    return this.httpClient.post(`${this.databaseURL}`, user);


  }












}
