import { Component, OnInit } from '@angular/core';
import {Leaguedata} from "../../../models/leaguedata";
import {ShowleagueserviceService} from "../../../services/showleagueservice.service";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {BettingRound} from "../../../models/betting-round";
import {TipprundenserviceService} from "../../../services/tipprundenservice.service";

@Component({
  selector: 'app-meinetipprunden',
  templateUrl: './meinetipprunden.component.html',
  styleUrls: ['./meinetipprunden.component.css']
})
export class MeinetipprundenComponent implements OnInit {

  mytiprounds: BettingRound[] | any;
  matchDayCount:Array<number> = new Array(34);
  matchDayDaten:Leaguedata[] | any;
  _ligaID!:number;


  constructor(private tipprundenservice:TipprundenserviceService, private showleaguedataservice:ShowleagueserviceService, private fb:FormBuilder) { }

  ngOnInit(): void {
   this.ArrayFüllen()
    this.zeigeMeineTipprunden()

  }
  tippPattern ='^[0-9]+[-]+[0-9]*$';
  TippAbgabeForm= new FormGroup({
    tippAbgabe: new FormControl('', Validators.compose([
      Validators.required,
      Validators.pattern(this.tippPattern)]))
  })

  get f() {
    return this.TippAbgabeForm.controls;
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
  SaveligaID(ligaID:number){
    this._ligaID=ligaID;
  }
  MatchDayDaten(matchDayID:number){
    this.showleaguedataservice.getAllMatchDayDaten(this._ligaID,matchDayID).subscribe(res=>{

      this.matchDayDaten=res;
    })

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