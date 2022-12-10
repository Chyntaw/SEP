import { Injectable } from '@angular/core';
import { HttpClient, HttpRequest, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';
import {Liga} from "../../models/liga";
import {Leaguedata} from "../../models/leaguedata";
import {User} from "../../models/roles/user/user";

@Injectable({
  providedIn: 'root'
})
export class ShowTopService {
  private baseUrl = 'http://localhost:8080/bets/';

  constructor(private http: HttpClient) { }

  getAllLeagues() {
    return this.http.get<Liga[]>(this.baseUrl + "leagues")
  }

  getTopUser(id: number) {
    return this.http.get<User[]>(this.baseUrl + "topUser/" + String(id))
  }

  getTopTeams(id: number) : Observable<Leaguedata[]>{
    return this.http.get<Leaguedata[]>(this.baseUrl + "topTeams/" + String(id))
  }
}
