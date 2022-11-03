import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {User} from "../models/user";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class RegistrationserviceService {

  private databaseURL="http://localhost:8080/user/add"

  constructor(private httpClient: HttpClient) { }

  addUser(user: User):Observable<object>{

    //httpClient callen
    console.log(user)
    return this.httpClient.post(`${this.databaseURL}`, user);

  }


}
