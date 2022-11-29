import { Component, OnInit } from '@angular/core';
import {BettingRound} from "../../../models/betting-round";
import {TipprundenserviceService} from "../../../services/tipprundenservice.service";
import {Leaguedata} from "../../../models/leaguedata";

@Component({
  selector: 'app-tipprundenuebersicht',
  templateUrl: './tipprundenuebersicht.component.html',
  styleUrls: ['./tipprundenuebersicht.component.css']
})
export class TipprundenuebersichtComponent implements OnInit {

  tiprounds: BettingRound[] | any;



  constructor(private tipprundenService:TipprundenserviceService) { }

  ngOnInit(): void {
    this.zeigeTipprunden();
  }

  zeigeTipprunden(){
    this.tipprundenService.getAllPublicTipprunden().subscribe(res=>{
      console.log(res)
      this.tiprounds=res;
    })

  }
  tipprundeBeitreten(name:string){

  }
}
