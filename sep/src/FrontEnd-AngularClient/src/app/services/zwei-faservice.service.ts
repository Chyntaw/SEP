import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {User} from "../models/roles/user/user";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ZweiFaserviceService {

  private databaseURL="http://localhost:8080/user/2FA"

  constructor(private httpClient: HttpClient) { }

  zweiFaUser(user:User):Observable<object>{
    console.log(user)
    return this.httpClient.post(`${this.databaseURL}`, user);


  }

}
