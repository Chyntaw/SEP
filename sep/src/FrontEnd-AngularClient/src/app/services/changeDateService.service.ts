import { Injectable } from '@angular/core';
import {HttpClient, HttpRequest} from '@angular/common/http';
import { Observable } from 'rxjs';
import {SystemDatum} from "../models/SystemDatum";

@Injectable({
  providedIn: 'root'
})
export class ChangeDateServiceService {

  private databaseURL = 'http://localhost:8080';

  constructor(private httpClient: HttpClient) { }

  changeDate(date: string):Observable<object>{

    const formData: FormData = new FormData();
    formData.append('date', date);

    return this.httpClient.post(`${this.databaseURL}/systemdatum/update`, formData);


  }
  getDate(){

   return this.httpClient.get(`${this.databaseURL}/systemdatum/getDate`)

  }

}
