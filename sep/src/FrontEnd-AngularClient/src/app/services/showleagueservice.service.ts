import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {Leaguedata} from "../models/leaguedata";
import {Liga} from "../models/liga";



@Injectable({
  providedIn: 'root'
})
export class ShowleagueserviceService {

  private databaseURL="http://localhost:8080/"


  constructor(private http:HttpClient) { }


 public getAll(id:number): Observable<Leaguedata[]>
 {

   return this.http.get<Leaguedata[]>(`${this.databaseURL+'leagueData/getAll/'+id}`)
 }

  public findAll(): Observable<Liga[]>
  {

    return this.http.get<Liga[]>(`${this.databaseURL+"liga/findAll"}`)
  }

}
