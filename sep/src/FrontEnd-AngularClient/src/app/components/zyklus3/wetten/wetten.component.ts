import { Component, OnInit } from '@angular/core';
import {Router} from "@angular/router";
import {GetUserServiceService} from "../../../services/getUserService.service";
import {FriendListService} from "../../../services/friend-list.service";
import {TipprundenserviceService} from "../../../services/tipprundenservice.service";
import {FreischaltungenService} from "../../../services/freischaltungen.service";
import {User} from "../../../models/roles/user/user";
import {Liga} from "../../../models/liga";
import {ShowleagueserviceService} from "../../../services/showleagueservice.service";
import {Leaguedata} from "../../../models/leaguedata";
import {WettenService} from "../../../services/wetten.service";
import {error} from "@angular/compiler-cli/src/transformers/util";


@Component({
  selector: 'app-wetten',
  templateUrl: './wetten.component.html',
  styleUrls: ['./wetten.component.css']
})
export class WettenComponent implements OnInit {

  constructor(private getUserService: GetUserServiceService,
              private router: Router,
              private freischaltungsService: FreischaltungenService,
              private showleagueservice: ShowleagueserviceService,
              private wettenService: WettenService) { }

  me: User = new User();

  //Logik Beantragung

  isFreigeschaltet: boolean|any
  isBeantragt: boolean|any
  buttonVisibleIfOldAndNotBeantragt: boolean|any
  isOld: boolean|any
  money:number = 0


  ligen: Liga [] | any;
  data: Leaguedata[] | any;

  oddsList: String[]|any

  qouteButton: number|any;
  wettEinsatz: number|any;
  leagueDataIDzumWetten: number|any
  tipp:number|any             //0 = Sieg Mannschaft 1, 1 = Unentschieden, 2 = Sieg Mannschaft 2


  futureWetten : String[]|any;
  pastWetten : String[]|any;



  ngOnInit(): void {
    this.getUser()
    this.isOldEnough()
    this.istFreigeschaltet()

    this.zeigeLigen()

    this.getFutureWetten()
    this.getPastWetten()
  }
  getUser(): void {

    this.me.firstName = String(sessionStorage.getItem("firstName"))
    this.me.lastName = String(sessionStorage.getItem("lastName"))
    this.me.eMail = String(sessionStorage.getItem("eMail"))
  }

  logout() {
    sessionStorage.clear()
    this.router.navigate(['/login'])
  }

  isOldEnough(){
    this.getUserService.isOldEnough(this.me.eMail).subscribe(res => {
      if(res.toString() == "true"){
        this.istBeantragt()
        this.isOld=true
      }
      else{
        this.buttonVisibleIfOldAndNotBeantragt = false;
        this.isOld=false
      }
    })
  }

  istBeantragt(){
    this.freischaltungsService.isFreischaltungBeantragt(this.me.eMail).subscribe(res=>{
      if(res.toString() == "true"){
        this.isBeantragt = true
        this.buttonVisibleIfOldAndNotBeantragt = false;
      }
      else{
        this.isBeantragt = false;
        this.buttonVisibleIfOldAndNotBeantragt = true;
      }
    })
  }

  beantrageFreischaltung(){
    this.isBeantragt = true;
    this.freischaltungsService.beantrageFreischaltung(this.me.eMail).subscribe(res=>{
    })
  }

  istFreigeschaltet(){
    this.freischaltungsService.isFreigeschaltet(this.me.eMail).subscribe(res=>{
      if(res.toString() == "true"){
        this.isFreigeschaltet = true;
      }
      else{
        this.isFreigeschaltet = false;
      }
    })
  }

  getMoney(){
    this.getUserService.getMoney(this.me.eMail).subscribe(res=>{

      this.money = parseFloat(res.toString());
    })
  }

  zeigeLigen() {
    this.showleagueservice.findAll().subscribe(res => {
      this.ligen = res
    })

  }

  getNext(curr:string) {
    return String(Number(curr)+1)
  }
  zeigeLigaDaten(id: number) {
    this.showleagueservice.getAll(id).subscribe(res => {
      this.data = res
      this.getOddsForLiga(id)
    })
  }

  getOddsForLiga(id:number){
    this.showleagueservice.getOddsForLiga(id).subscribe(res=>{
      this.oddsList = res

    })
  }

  wetteSetzen(){
    if(this.wettEinsatz > this.money){
      alert("Ihr Guthaben reicht nicht aus, bitte setzen Sie einen kleineren Wetteinsatz!")
    }
    else if(this.qouteButton == null || this.leagueDataIDzumWetten == null){
      alert("Wählen Sie zuerst eine Qoute aus!")
    }
    else{
      this.wettenService.setzeWette(this.me.eMail, this.wettEinsatz, this.qouteButton, this.leagueDataIDzumWetten, this.tipp).subscribe(res => {
        alert("Wette Platziert")
        window.location.reload()

      }, error=>alert("Wetten auf vergangene Spiele nicht möglich!"));
    }
  }

  qouteWaehlen(qoute:number, leagueID:number, tipp:number){
    this.qouteButton = qoute;
    this.leagueDataIDzumWetten = leagueID
    this.tipp = tipp
  }


  getFutureWetten(){
    this.wettenService.getFutureWetten(this.me.eMail).subscribe(res => {
      this.futureWetten = res;
    })
  }

  getPastWetten(){
    this.wettenService.getPastWetten(this.me.eMail).subscribe(res => {
      this.pastWetten = res;
    },() =>{}, () => this.getMoney())
  }



}
