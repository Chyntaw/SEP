import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {BettingRound} from "../models/betting-round";

@Injectable({
  providedIn: 'root'
})
export class TipprundenserviceService {

  private baseUrl = 'http://localhost:8080/bettingRound';
  public _tipprunde!:BettingRound;

  constructor(private http:HttpClient) { }


  createTipprunde(ligaID: number, ownerID: number, name: string, isPrivate: boolean, corrScorePoints: number, corrGoalPoints: number,

                  corrWinnerPoints: number, passwordTipprunde: string | undefined):Observable<any>{

    const formData: FormData = new FormData();
    formData.append('ownerID', String(ownerID));
    formData.append('ligaID',  String(ligaID));
    formData.append('name',name)
    formData.append('isPrivate', String(isPrivate));
    formData.append('corrScorePoints', String(corrScorePoints));
    formData.append('corrGoalPoints', String(corrGoalPoints));
    formData.append('corrWinnerPoints', String(corrWinnerPoints));
    formData.append('password', String(passwordTipprunde));
    console.log(formData)

    return this.http.post(`${this.baseUrl}/create`, formData)
  }
  getAllPublicTipprunden():Observable<any>{

    return this.http.get(`${this.baseUrl}/getAllPublicRounds`)

  }
  getRoundsbyUserID(id:number){
    console.log(id)
    return this.http.get(`${this.baseUrl}/getRoundsbyUserID/`+id)

  }

  getTipprundeByLigaID(bettingroundID:number, userID:number) {
    return this.http.get(`${this.baseUrl}/getTipprundeByLigaID/`+userID+'/'+bettingroundID);
  }

  transferBets(fromTipprundenID:number, toTipprundenID:number, userID:number) {
    return this.http.get(`${this.baseUrl}/transferBets/`+fromTipprundenID+`/`+toTipprundenID+`/`+userID);
  }



  getTipprunde() {

    console.log(this._tipprunde)
    return this._tipprunde;
  }

  setTipprunde(value: BettingRound) {
    this._tipprunde = value;
    console.log(this._tipprunde)
  }


  getTipHelpByTeams(player1: string, player2: string, id: number) {
    const formData: FormData = new FormData();
    formData.append("player1", player1)
    formData.append("player2", player2)
    formData.append("id", String(id))
    return this.http.post(`${this.baseUrl}/getTipHelp`, formData)
  }

  getBetsByLeagueDataID(userID:number, leagueDataids:Number[], bettingRoundID:number): Observable<any> {
    const formData: FormData = new FormData();
    formData.append("userID", String(userID));
    formData.append("leagueDataids", String(leagueDataids));
    formData.append("bettingRoundID", String(bettingRoundID));
    return this.http.put(`${this.baseUrl}/getBetsByLeagueDataID`, formData);
  }

    placeBet(bettingRoundid:number, userid:number, leagueDataid:number, newBet:string):Observable<any>{
      const formData: FormData = new FormData();
      formData.append("bettingRoundid",String(bettingRoundid))
      formData.append("userid",String(userid))
      formData.append("leagueDataid",String(leagueDataid))
      formData.append("newBet",newBet)


   return this.http.post(`${this.baseUrl}/placeBet`, formData)

  }
  addParticipant(userid:number, bettingRoundid:number):Observable<any>{
    const formData: FormData = new FormData();
    formData.append("bettingRoundid",String(bettingRoundid))
    formData.append("userid",String(userid))

    return this.http.post(`${this.baseUrl}/addParticipant`, formData)
  }
  findByName(searchInput:string){
    console.log(searchInput)
    return this.http.get<BettingRound[]>(this.baseUrl+"/name/"+searchInput);
  }
}

