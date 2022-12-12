import { Component, OnInit } from '@angular/core';
import {BettingRound} from "../../../models/betting-round";
import {TipprundenserviceService} from "../../../services/tipprundenservice.service";
import {Leaguedata} from "../../../models/leaguedata";
import { Pipe, PipeTransform } from '@angular/core';
import { transform } from 'typescript';
import {User} from "../../../models/roles/user/user";
import {FriendListService} from "../../../services/friend-list.service";
import {Router} from "@angular/router";
import {SystemDatum} from "../../../models/SystemDatum";
import {ChangeDateServiceService} from "../../../services/changeDateService.service";

@Component({
  selector: 'app-tipprundenuebersicht',
  templateUrl: './tipprundenuebersicht.component.html',
  styleUrls: ['./tipprundenuebersicht.component.css']
})
export class TipprundenuebersichtComponent implements OnInit {

  tiprounds: BettingRound[] | any;
  privateTiprounds: BettingRound[] | any;
  ArrayWithTiproundsOwnerIDS : Number[] = [];
  ArrayWithPrivateTiproundsOwnerIDS : Number[] = [];
  CurrentUserID!:number;
  CurrentTiproundID!:number;
  searchInput!:any
  searchInput2!:any
  user:User = new User();
  friends: User [] | any;
  selectedFriend: string | undefined;
  currTippRoundHasPassword!:boolean;
  alias!:string;
  password:string="";
  aktuellesDatum: SystemDatum | any;
  allUser: User[] | any;



  constructor(private tipprundenService:TipprundenserviceService, private friendListService: FriendListService, private router:Router,private changeDateService: ChangeDateServiceService) { }

  ngOnInit(): void {
    this.getDatum();
    this.zeigeTipprunden();
    this.zeigePrivateTipprunden();
    this.CurrentUserID=Number(sessionStorage.getItem("id"))
    this.searchFriends();
    this.findAllUser();
  }

  findAllUser(){
    this.friendListService.findAllUser().subscribe(res=>{
      this.allUser = res;
    })
  }
  getDatum(): void {
    this.changeDateService.getDate().subscribe(res => {
      this.aktuellesDatum = res
    })
  }

  zeigeTipprunden(){
    this.tipprundenService.getAllPublicTipprunden().subscribe(res=>{
      console.log(res)
      this.tiprounds=res;

      for(var value of this.tiprounds) {
        this.ArrayWithTiproundsOwnerIDS.push(value.ownerID);
      }
      console.log(this.ArrayWithTiproundsOwnerIDS);


    })
  }
  zeigePrivateTipprunden(){
     let userid = Number(sessionStorage.getItem('id'))
    this.tipprundenService.getAllPrivateTipproundsByEmail(userid).subscribe(res=>{
      console.log(res);
      this.privateTiprounds=res;
      for(var value of this.privateTiprounds) {
        this.ArrayWithPrivateTiproundsOwnerIDS.push(res.ownerID);
      }
      console.log(this.ArrayWithPrivateTiproundsOwnerIDS)
    })
  }


  tipprundeBeitreten(bettingroundid:number){
    console.log(this.CurrentTiproundID)
    console.log(bettingroundid)

  this.tipprundenService.addAlias(this.alias,this.CurrentUserID,bettingroundid).subscribe(res=>{

    })

    let userid = Number(sessionStorage.getItem("id"))
    this.tipprundenService.addParticipant(userid,bettingroundid,this.password).subscribe(res=>{

      alert("Beitritt erfolgreich")

    },error =>alert("Beitritt nicht erfolgreich"))



  }


  tipprundeEinladen(bettingroundid:number){
    const userEmail = sessionStorage.getItem("eMail")
    if(userEmail && this.selectedFriend){
      this.friendListService.tipprundeEinladen(bettingroundid, userEmail, this.selectedFriend).subscribe()
    }

  }



  searchFriends(){
    const email:string | null = sessionStorage.getItem('eMail');

    if(email){
      this.friendListService.showFriends(email).subscribe(data=>{
        this.friends = data;
      })
    }
  }

  onSelected(selectedFriendEmail: string){
    this.selectedFriend = selectedFriendEmail;
  }
  saveTiproundID(tiproundID:number){

    this.CurrentTiproundID=tiproundID;
  }
  GetCurrTippRound(tiproundID:number){

    this.tipprundenService.getTipproundByID(tiproundID).subscribe(res=>{
      console.log(res.password)


          if(res.password=="" || res.password=="undefined"){
            this.currTippRoundHasPassword=true;
          }
          else{
            this.currTippRoundHasPassword=false
          }

    })
  }
  logout() {
    sessionStorage.clear()
    this.router.navigate(['/login'])
  }
}
