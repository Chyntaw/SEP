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
  private baseUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) { }

  getTopUser() {
    return this.http.get<User[]>(this.baseUrl + "/bets/topUser")
  }

  getTopTeams() : Observable<Leaguedata[]>{
    return this.http.get<Leaguedata[]>(this.baseUrl + "/bets/topTeams")
  }
}
