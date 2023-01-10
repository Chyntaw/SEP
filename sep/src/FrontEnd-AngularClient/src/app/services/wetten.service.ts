import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";


@Injectable({
  providedIn: 'root'
})
export class WettenService {
  private databaseURL="http://localhost:8080/wetten/"

  constructor(private httpClient: HttpClient) { }

  setzeWette(eMail:string, wettEinsatz: number, qoute: number, leagueDataIDzumWetten: number, tipp:number) {
    return this.httpClient.get(`${this.databaseURL+"setzeWette/"+leagueDataIDzumWetten+"/"+eMail+"/"+qoute+"/"+wettEinsatz+"/"+tipp}`);
  }


  getFutureWetten(eMail: string) {
    return this.httpClient.get(`${this.databaseURL+"platzierteWetten/"+eMail}`);
  }
  getPastWetten(eMail: string) {
    return this.httpClient.get(`${this.databaseURL+"vergangeneWetten/"+eMail}`);
  }
}
