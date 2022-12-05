import { Component, OnInit } from '@angular/core';
import {Leaguedata} from "../../../models/leaguedata";
import {ShowleagueserviceService} from "../../../services/showleagueservice.service";
import { FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {BettingRound} from "../../../models/betting-round";
import {Bets} from "../../../models/bets";
import {TipprundenserviceService} from "../../../services/tipprundenservice.service";
import {Score} from "../../../models/score";

@Component({
  selector: 'app-meinetipprunden',
  templateUrl: './meinetipprunden.component.html',
  styleUrls: ['./meinetipprunden.component.css']
})
export class MeinetipprundenComponent implements OnInit {

  userid = Number(sessionStorage.getItem('id'));
  mytiprounds: BettingRound[] | any;
  matchDayCount:Array<number> = new Array(34);
  matchDayDaten:Leaguedata[] | any;
  Bets: String[] | any;
  _ligaID!:number;
  tipRundenID!: number;
  newBet:Bets = new Bets();
  selectedBettingRoundID: number | any;
  myTippRundenByLigaID: BettingRound[] | any;
  leaderboard!:Score[] | any;

  constructor(private tipprundenservice:TipprundenserviceService, private showleaguedataservice:ShowleagueserviceService, private fb:FormBuilder) { }

  ngOnInit(): void {
   this.ArrayFüllen()
    this.zeigeMeineTipprunden()


  }
  tippPattern ='^[0-9]+[-][0-9]+$';
  TippAbgabeForm= new FormGroup({
    tippAbgabe: new FormControl('', Validators.compose([
      Validators.required,
      Validators.pattern(this.tippPattern)]))
  })

  get f() {
    return this.TippAbgabeForm.controls;
  }
  get tippAbgabeWert(){
    return this.TippAbgabeForm.get("tippAbgabe")
  }
  FormControlReset(){
    this.TippAbgabeForm.controls.tippAbgabe.setValue(null)
}

  getTippRundenByLigaID(bettingroundID:number) {
    this.tipprundenservice.getTipprundeByLigaID(bettingroundID, this.userid).subscribe(res => {
      this.myTippRundenByLigaID = res;
      this.selectedBettingRoundID = bettingroundID;
      }

    )
  }

  zeigeMeineTipprunden(){

    this.tipprundenservice.getRoundsbyUserID(this.userid).subscribe(res=>{
          this.mytiprounds=res;
    })


  }
  ArrayFüllen(){
    for(let i=1;i<this.matchDayCount.length;i++){
        this.matchDayCount[i]=i;
    }
  }
  SaveligaID(ligaID:number, tippRundenid:number){
    this._ligaID=ligaID;
    this.tipRundenID = tippRundenid;
  }
  MatchDayDaten(matchDayID:number){
    this.showleaguedataservice.getAllMatchDayDaten(this._ligaID,matchDayID).subscribe(res=>{

      this.matchDayDaten=res;
      let arr1 : Number[] = [];
      for(var val of this.matchDayDaten) {
        arr1.push(val.id);
      }
      console.log(arr1);
      this.getBets(this.userid, arr1, this.tipRundenID)
    })

  }
  getBets(userID:number, leagueDataIDs:Number[], bettingRoundID:number) {
    this.tipprundenservice.getBetsByLeagueDataID(userID, leagueDataIDs, bettingRoundID).subscribe(res => {
      console.log(res)
      this.Bets = res;
    })
  }


// show TipHelp
  getTipHelp(player1: string, player2: string, id: number) {
    this.tipprundenservice.getTipHelpByTeams(player1, player2, id).subscribe(data => {
      let leaguedataResult = <Leaguedata>data
      if("N/A" == leaguedataResult.result) {
        alert("Keine Tipphilfe derzeit verfügbar")
      }
      else {
        alert("Unser Tipp: " + leaguedataResult.result)
      }
    })
  }
  placeBet(leagueDataid:number, index:number){

    this.tipprundenservice.placeBet(this.tipRundenID, this.userid, leagueDataid,this.matchDayDaten[index].newBet).subscribe(res=>{
      alert("Tipp gespeichert!")
    },error=>alert("Tipp konnte nicht gespeichert werden"));
  }


  validateNo(e:any): boolean {
    const charCode = e.which ? e.which : e.keyCode;
    if(charCode==45) return true;
    else if (charCode > 31 && (charCode < 48 || charCode > 57 )){
      return false
    }
    return true
  }

  transferBets(toTipprundenID:number) {
    this.tipprundenservice.transferBets(this.selectedBettingRoundID, toTipprundenID, this.userid).subscribe(res => {
      alert("Tipps übernommen!")
    },error=>alert("Fehler beim übernehmen der Tipps"));
  }
  getLeaderBoard(tipprundenID:number){

    this.tipprundenservice.getLeaderBoard(tipprundenID).subscribe(res=>{
      console.log(res)
      this.leaderboard=res;
      console.log(this.leaderboard)
    })

}
}
