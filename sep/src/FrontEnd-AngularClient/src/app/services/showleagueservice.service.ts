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
  private _id!:number;

  constructor(private http:HttpClient) { }


  public getAll(id:number): Observable<Leaguedata[]>
  {
    return this.http.get<Leaguedata[]>(`${this.databaseURL+'leagueData/getAll/'+id}`)
  }

  public findAll(): Observable<Liga[]>
  {
    return this.http.get<Liga[]>(`${this.databaseURL+"liga/findAll"}`)
  }

  getAllMatchDayDaten(ligaID:number,matchDayID:number):Observable<any>{
    return this.http.get(`${this.databaseURL+'leagueData/getByMatchday/'+ligaID+'/'+matchDayID}`)
  }

  getDisabledButtons() {
    return this.http.get(`${this.databaseURL+"liga/buttondisabler"}`)
  }

  public findLigaByID(ligaID:number): Observable<Liga>
  {
    return this.http.get<Liga>(`${this.databaseURL+"leagueData/findLigaByID/"+ligaID}`)
  }

  get id(): number {
    return this._id;
  }

  set id(value: number) {
    this._id = value;
  }
}
