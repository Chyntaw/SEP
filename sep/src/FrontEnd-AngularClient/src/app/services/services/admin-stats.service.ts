import { Injectable } from '@angular/core';
import { HttpClient, HttpRequest, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';
import {Liga} from "../../models/liga";
import {Leaguedata} from "../../models/leaguedata";
import {User} from "../../models/roles/user/user";

@Injectable({
  providedIn: 'root'
})
export class AdminStatsService {
  private baseUrl = 'http://localhost:8080/bets/';

  constructor(private http: HttpClient) { }

  getLeaguesSortedByBettingRound() {
    return this.http.get<Liga[]>(this.baseUrl + "LeaguesBettingRound")
  }

  getLeaguesSortedByUsers() {
    return this.http.get<Liga[]>(this.baseUrl + "LeaguesUsers")
  }

  putByBettingRound(ligaID: number) {
    return this.http.get<string>(this.baseUrl + "PutByTippingRound/" + ligaID)
  }

  putByBet(ligaID: number) {
    return this.http.get<string>(this.baseUrl + "PutByBet/" + ligaID)
  }

}
