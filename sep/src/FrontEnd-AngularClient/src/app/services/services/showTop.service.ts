import { Injectable } from '@angular/core';
import { HttpClient, HttpRequest, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';
import {Liga} from "../../models/liga";
import {Leaguedata} from "../../models/leaguedata";

@Injectable({
  providedIn: 'root'
})
export class ShowTopService {
  private baseUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) { }

  getTopUser() {

  }

  getTopTeams() : Observable<Leaguedata[]>{
    return this.http.get<Leaguedata[]>(this.baseUrl + "/bets/topTeams")
  }
}
