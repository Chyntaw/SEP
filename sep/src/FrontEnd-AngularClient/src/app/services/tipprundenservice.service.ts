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



  getTipprunde() {

    console.log(this._tipprunde)
    return this._tipprunde;
  }

  setTipprunde(value: BettingRound) {
    this._tipprunde = value;
    console.log(this._tipprunde)
  }



}
