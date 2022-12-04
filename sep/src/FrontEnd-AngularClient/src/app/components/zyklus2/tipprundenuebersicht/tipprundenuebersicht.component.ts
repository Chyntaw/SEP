import { Component, OnInit } from '@angular/core';
import {BettingRound} from "../../../models/betting-round";
import {TipprundenserviceService} from "../../../services/tipprundenservice.service";
import {Leaguedata} from "../../../models/leaguedata";
import { Pipe, PipeTransform } from '@angular/core';
import { transform } from 'typescript';
import {User} from "../../../models/roles/user/user";
import {FriendListService} from "../../../services/friend-list.service";

@Component({
  selector: 'app-tipprundenuebersicht',
  templateUrl: './tipprundenuebersicht.component.html',
  styleUrls: ['./tipprundenuebersicht.component.css']
})
export class TipprundenuebersichtComponent implements OnInit {

  tiprounds: BettingRound[] | any;
  ArrayWithTiproundsOwnerIDS : Number[] = [];
  CurrentUserID!:number;
  searchInput!:any
  searchInput2!:any

  user:User = new User();
  friends: User [] | any;

  selectedFriend: string | undefined;



  constructor(private tipprundenService:TipprundenserviceService, private friendListService: FriendListService) { }

  ngOnInit(): void {
    this.zeigeTipprunden();
    this.CurrentUserID=Number(localStorage.getItem("id"))
    this.searchFriends();
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
  tipprundeBeitreten(bettingroundid:number){
    let userid = Number(localStorage.getItem("id"))
    this.tipprundenService.addParticipant(userid,bettingroundid).subscribe(res=>{

      alert("Beitritt erfolgreich")

    },error =>alert("Beitritt nicht erfolgreich"))



  }


  tipprundeEinladen(bettingroundid:number){
    const userEmail = localStorage.getItem("eMail")
    if(userEmail && this.selectedFriend){
      this.friendListService.tipprundeEinladen(bettingroundid, userEmail, this.selectedFriend).subscribe()
    }

  }



  searchFriends(){
    const email:string | null = localStorage.getItem('eMail');

    if(email){
      this.friendListService.showFriends(email).subscribe(data=>{
        console.log(data)
        this.friends = data;
      })
    }
  }

  onSelected(selectedFriendEmail: string){
    this.selectedFriend = selectedFriendEmail;
  }



}
