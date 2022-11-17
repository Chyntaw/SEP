import { Injectable } from '@angular/core';
import {HttpClient, HttpEvent, HttpRequest} from "@angular/common/http";
import {User} from "../models/roles/user/user";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class RegistrationserviceService {

  private databaseURL="http://localhost:8080/user/add"

  constructor(private httpClient: HttpClient) { }

  addUser(file: File, lastName: string, firstName: string, birthDate: string, eMail: string, password: string, role: string):Observable<object>{

    const formData: FormData = new FormData();
    formData.append('file', file);
    formData.append('lastName', lastName);
    formData.append('firstName', firstName);
    formData.append('birthDate', birthDate);
    formData.append('eMail', eMail);
    formData.append('password', password);
    formData.append('role', role);

    return this.httpClient.post(`${this.databaseURL}`, formData);
  }

  addUserOhnePB(lastName: string, firstName: string, birthDate: string, eMail: string, password: string, role: string):Observable<object>{

    const formData: FormData = new FormData();
    formData.append('lastName', lastName);
    formData.append('firstName', firstName);
    formData.append('birthDate', birthDate);
    formData.append('eMail', eMail);
    formData.append('password', password);
    formData.append('role', role);

    return this.httpClient.post(`${this.databaseURL}`, formData);
  }

  addAdmin(lastName: string, firstName: string, eMail: string, password: string, role: string):Observable<object>{

    const formData: FormData = new FormData();
    formData.append('lastName', lastName);
    formData.append('firstName', firstName);
    formData.append('eMail', eMail);
    formData.append('password', password);
    formData.append('role', role);


    return this.httpClient.post(`${this.databaseURL}`, formData);
  }
}
