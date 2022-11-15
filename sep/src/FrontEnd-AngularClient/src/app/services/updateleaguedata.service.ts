// @ts-ignore

import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class UpdateleaguedataService {

  private baseUrl = 'http://localhost:8080/leagueData/';

  constructor(private http:HttpClient) { }




  updateLeagueData(id: number, value: object): Observable<Object> {

      return this.http.put(`${this.baseUrl}update/${id}`, value);



  }


  getLeagueData(id: number): Observable<Object> {
    return this.http.get(`${this.baseUrl}`+id);
  }
}
