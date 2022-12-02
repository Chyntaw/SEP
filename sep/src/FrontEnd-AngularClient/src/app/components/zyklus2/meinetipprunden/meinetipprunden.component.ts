import { Component, OnInit } from '@angular/core';
import {Leaguedata} from "../../../models/leaguedata";
import {ShowleagueserviceService} from "../../../services/showleagueservice.service";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {BettingRound} from "../../../models/betting-round";
import {Bets} from "../../../models/bets";
import {TipprundenserviceService} from "../../../services/tipprundenservice.service";
import {error} from "@angular/compiler-cli/src/transformers/util";

@Component({
  selector: 'app-meinetipprunden',
  templateUrl: './meinetipprunden.component.html',
  styleUrls: ['./meinetipprunden.component.css']
})
export class MeinetipprundenComponent implements OnInit {

  mytiprounds: BettingRound[] | any;
  matchDayCount:Array<number> = new Array(34);
  matchDayDaten:Leaguedata[] | any;
  Bets: String[] | any;
  _ligaID!:number;
  tipRundenID!: number;
  newBet:Bets = new Bets();
  newBets:Bets[] | any;


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

  zeigeMeineTipprunden(){

    let userid = Number(localStorage.getItem('id'))

    this.tipprundenservice.getRoundsbyUserID(userid).subscribe(res=>{

        console.log(res)

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
      let userid = Number(localStorage.getItem('id'))
      this.getBets(userid, arr1, this.tipRundenID)
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

  placeBet(leagueDataid:number){
   let bet:any;

    bet=this.tippAbgabeWert?.value;

    let userid = Number(localStorage.getItem('id'))

    this.tipprundenservice.placeBet(this.tipRundenID, userid, leagueDataid,bet).subscribe(res=>{
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

}
