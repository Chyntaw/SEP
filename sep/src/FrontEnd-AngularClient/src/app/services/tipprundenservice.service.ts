import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {BettingRound} from "../models/betting-round";

@Injectable({
  providedIn: 'root'
})
export class TipprundenserviceService {

  private baseUrl = 'http://localhost:8080/bettingRound';


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
    if(passwordTipprunde=="")
    formData.append('password', "undefined");
    else formData.append('password', String(passwordTipprunde))
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


  getTipHelpByTeams(id: number) {
    const formData: FormData = new FormData();
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
  addParticipant(userid:number, bettingRoundid:number, password:string):Observable<any>{
    const formData: FormData = new FormData();
    formData.append("bettingRoundid",String(bettingRoundid))
    formData.append("userid",String(userid))
    formData.append("password", password)

    console.log(formData)

    return this.http.post(`${this.baseUrl}/addParticipant`, formData)
  }
  findByName(searchInput:string){
    console.log(searchInput)
    return this.http.get<BettingRound[]>(this.baseUrl+"/name/"+searchInput);
  }

  getLeaderBoard(bettingroundid:number):Observable<any>{

    return this.http.get(`${this.baseUrl}/leaderboard/`+bettingroundid)

  }
  getTipproundByID(bettingRoundID:number):Observable<any>{

    return this.http.get(`${this.baseUrl}/getTippRoundByID/`+bettingRoundID)
  }
  getAllPrivateTipproundsByEmail(userid:number):Observable<any>{

    return this.http.get(`${this.baseUrl}/getAllPrivateRounds/`+userid)
  }
  getOwnedTipprunden(userid: number):Observable<any> {
    return this.http.get(`${this.baseUrl}/getAllOwnedRounds/`+userid);
  }
  addAlias(alias:string, userID:number,bettingroundID:number){
    const formData: FormData = new FormData();
    formData.append("bettingroundID",String(bettingroundID));
    formData.append("userID",String(userID));
    formData.append("alias",alias);

    return this.http.put(`${this.baseUrl}/changeAlias`,formData)
  }
  getMachDateBoolean(gameDate:string):Observable<boolean>{
  console.log(gameDate)
    return this.http.get<boolean>(`${this.baseUrl}/getDisabled/`+gameDate)
  }
  getDisabled(date:string[]) {
    const data: FormData = new FormData();
    data.append("date", String(date));
    console.log(data)
    return this.http.post(`${this.baseUrl}/getDisabled`, data);
  }

  getUserBettingTable(bettingroundID:number, userID:number) {
    return this.http.get(`${this.baseUrl}/getUserBettingTable/`+userID+'/'+bettingroundID);
  }

  getOwnTippsPerRound(userID:number) {
    return this.http.get(`${this.baseUrl}/getRoundsWithUserTipps/`+userID)
  }


}

