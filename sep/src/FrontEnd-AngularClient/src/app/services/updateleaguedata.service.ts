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


  /*getLeagueDataList(): Observable<any> {
    return this.http.get(`${this.baseUrl}`+'leagueData');
  }

  createStudent(student: object): Observable<object> {
    return this.http.post(`${this.baseUrl}`+'save-student', student);
  }

  deleteStudent(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/delete-student/${id}`, { responseType: 'text' });
  }

  getStudent(id: number): Observable<Object> {
    return this.http.get(`${this.baseUrl}/student/${id}`);
  }*/




  updateLeagueData(id: number, value: object): Observable<Object> {

      return this.http.put(`${this.baseUrl}update/${id}`, value);



  }
//muss richtige URL hin
  getLeagueDataList(): Observable<any> {
    return this.http.get(`${this.baseUrl}`+'leagueData');
  }

  getLeagueData(id: number): Observable<Object> {
    return this.http.get(`${this.baseUrl}`+id);
  }
}
