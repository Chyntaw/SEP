import { Component, OnInit } from '@angular/core';
import {User} from "../../../../models/roles/user/user";
import {GetUserServiceService} from "../../../../services/getUserService.service";
import {FriendListService} from "../../../../services/friend-list.service";
import {BettingRound} from "../../../../models/betting-round";
import {TipprundenserviceService} from "../../../../services/tipprundenservice.service";
import {Table} from "../../../../models/table";


@Component({
  selector: 'app-my-profile',
  templateUrl: './my-profile.component.html',
  styleUrls: ['./my-profile.component.css']
})
export class MyProfileComponent implements OnInit {

  me: User | any;
  retrievedImage: string | any;
  base64Data: string | any;
  logo: any
  existsImage:boolean = false;
  friends: User [] | any;
  userToShow!: User | any
  retrieveResonse: string | any;
  mytiprounds: BettingRound | any;
  myTipTable: Table[] | any


  constructor(private getUserService: GetUserServiceService,
              private friendListService: FriendListService,
              private tipprundenservice: TipprundenserviceService) { }

  ngOnInit(): void {
    this.getUser()
    this.showFriendList()
    this.zeigeMeineTipprunden()
  }

  getUser(): void{
    this.getUserService.getUser(String(sessionStorage.getItem("eMail"))).subscribe(res=>{
      this.me = res
      this.getImage();
    })
    }

  getImage(){
    this.getUserService.getImage(this.me.eMail).subscribe(res=>{
      console.log(res);
      this.retrieveResonse = res;
      if(this.retrieveResonse.picByte != null) {
      this.base64Data = this.retrieveResonse.picByte;
      this.retrievedImage = 'data:image/jpeg;base64,' + this.base64Data;
      }
  })}

  showFriendList() {
    const email = sessionStorage.getItem('eMail');
    if (email) {
      this.friendListService.showFriends(email).subscribe(data => {
        console.log(data)
        this.friends = data;
        console.log(this.friends)
      });
    }
  }

  zeigeMeineTipprunden() {
    this.tipprundenservice.getRoundsbyUserID(Number(sessionStorage.getItem("id"))).subscribe(res => {
      this.mytiprounds = res;
      console.log(this.mytiprounds)
    })
  }
  getTippRundenByLigaID(bettingroundID: number) {
    this.tipprundenservice.getUserBettingTable(bettingroundID, Number(sessionStorage.getItem("id"))).subscribe(res => {
        this.myTipTable = res;
      }
    )
  }

}
