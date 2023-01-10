import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";


@Injectable({
  providedIn: 'root'
})
export class WettenService {
  private databaseURL="http://localhost:8080/wetten/"

  constructor(private httpClient: HttpClient) { }

  setzeWette(eMail:string, wettEinsatz: number, qoute: number, leagueDataIDzumWetten: number) {
    console.log(eMail)
    return this.httpClient.get(`${this.databaseURL+"setzeWette/"+leagueDataIDzumWetten+"/"+eMail+"/"+qoute+"/"+wettEinsatz}`);
  }

}
