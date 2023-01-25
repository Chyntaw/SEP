import { Component, OnInit } from '@angular/core';
import {User} from "../../../../models/roles/user/user";
import {GetUserServiceService} from "../../../../services/getUserService.service";
import {FriendListService} from "../../../../services/friend-list.service";
import {BettingRound} from "../../../../models/betting-round";
import {TipprundenserviceService} from "../../../../services/tipprundenservice.service";
import {Table} from "../../../../models/table";
import {Router} from "@angular/router";
import {FreischaltungenService} from "../../../../services/freischaltungen.service";
import {Chart} from "chart.js/auto";


@Component({
  selector: 'app-my-profile',
  templateUrl: './my-profile.component.html',
  styleUrls: ['./my-profile.component.css']
})
export class MyProfileComponent implements OnInit {

  me: User = new User();
  retrievedImage: string | any;

  logo: any
  existsImage:boolean = false;
  friends: User [] | any;
  pendingFriends: User[] | any;
  pendingRequestedFriends: User[] | any;

  userToShow!: User | any
  mytiprounds: BettingRound | any;
  myTipTable: Table[] | any

  searchUserMail: User = new User();

  retrieveResonse: any;
  friendsPictures: string[] = [];
  base64Data: any;
  keinBildVorhanden: string = "data:image/jpeg;base64,null";

  //Charts
  TippNumChart: any;
  OwnTippsPerRound : any;
  PieChart: any;
  PieData: any;



  constructor(private getUserService: GetUserServiceService,
              private friendListService: FriendListService,
              private tipprundenservice: TipprundenserviceService,
              private router: Router,
              private freischaltungsService: FreischaltungenService) { }

  ngOnInit(): void {
    this.getOwnedTipprunden()
    this.getUser()
    this.showFriendList()
    this.zeigeMeineTipprunden()

    this.showPendingFriendList(this.me.eMail);
    this.showSendedFriendRequest(this.me.eMail);
  }

  getUser(): void{
    this.me.firstName = String(sessionStorage.getItem("firstName"))
    this.me.lastName = String(sessionStorage.getItem("lastName"))
    this.me.eMail = String(sessionStorage.getItem("eMail"))
    this.getImage()
    /*habs erstmal rausgenommen sonst klappt die freundesliste nicht wegen der tollen asynchronität :D
    this.getUserService.getUser(String(sessionStorage.getItem("eMail"))).subscribe(res=>{
      this.me = res
      this.getImage();
    })
     */
  }

  getImage(){
    this.getUserService.getImage(this.me.eMail).subscribe(res=>{
      this.retrieveResonse = res;
      if(this.retrieveResonse.picByte != null) {
        this.base64Data = this.retrieveResonse.picByte;
        this.retrievedImage = 'data:image/jpeg;base64,' + this.base64Data;
      }
    })
  }

  showFriendList() {
    const email = sessionStorage.getItem('eMail');
    if (email) {
      this.friendListService.showFriends(email).subscribe(data => {
        this.friends = data;
        console.log(this.friends);
        //this.getPicsOfFriends()
      });
    }
  }


  showPendingFriendList(email: string){
    this.friendListService.showPendingFriends(email).subscribe(data => {
      this.pendingFriends = data;
    });
  }

  showSendedFriendRequest(email: string){
    this.friendListService.showPendingFriendRequests(email).subscribe(data=>{
      this.pendingRequestedFriends = data;
    });
  }

  getPicsOfFriends() {
    for(let friends of this.friends) {
      this.friendListService.getImages(friends.eMail).subscribe(pic => {
        this.retrieveResonse = pic;
        if(this.retrieveResonse.picByte != null) {
        this.base64Data = this.retrieveResonse.picByte;
        this.friendsPictures.push('data:image/jpeg;base64,' + this.base64Data);
        } else {
          this.friendsPictures.push("null");
        }
      })
    }
  }


  showImageFriends(eMail: string){
    this.friendListService.getImages(eMail).subscribe(res=>{
      for(let friends of this.friends){
        if(friends.eMail == eMail){
          this.retrieveResonse = res;
          this.base64Data = this.retrieveResonse.picByte;
          friends.profilePictureName = 'data:image/jpeg;base64,' + this.base64Data
        }
      }
    })
  }
  showImagePendingFriends(eMail: string){
    this.friendListService.getImages(eMail).subscribe(res=>{
      for(let friends of this.pendingFriends){
        if(friends.eMail == eMail){
          this.retrieveResonse = res;
          this.base64Data = this.retrieveResonse.picByte;
          friends.profilePictureName = 'data:image/jpeg;base64,' + this.base64Data
        }
      }
    })
  }
  showImageRequestedFriends(eMail: string){
    this.friendListService.getImages(eMail).subscribe(res=>{
      for(let friends of this.pendingRequestedFriends){
        if(friends.eMail == eMail){
          this.retrieveResonse = res;
          this.base64Data = this.retrieveResonse.picByte;
          friends.profilePictureName = 'data:image/jpeg;base64,' + this.base64Data
        }
      }
    })
  }





  zeigeMeineTipprunden() {
    this.tipprundenservice.getRoundsbyUserID(Number(sessionStorage.getItem("id"))).subscribe(res => {
      this.mytiprounds = res;
    })
  }
  getTippRundenByLigaID(bettingroundID: number) {
    this.tipprundenservice.getUserBettingTable(bettingroundID, Number(sessionStorage.getItem("id"))).subscribe(res => {
        this.myTipTable = res;
      }
    )
  }

  showProfile(){
    this.router.navigate(['/getUser'], {queryParams: {eMail: this.searchUserMail.eMail}})
  }



  acceptFriend(friendEmail: string){
    const currentEmail: string | null = sessionStorage.getItem('eMail');
    if(currentEmail){
      this.friendListService.acceptFriend(currentEmail, friendEmail).subscribe(res=>{
      });
    }
  }

  declineFriend(friendEmail: string){
    const currentEmail: string | null = sessionStorage.getItem('eMail');
    if(currentEmail){
      this.friendListService.declineFriend(currentEmail, friendEmail).subscribe();
    }
  }

  removeFriend(friendEmail: string) {
    const currentEmail: string | null = sessionStorage.getItem('eMail')

    if(currentEmail){
      this.friendListService.removeFriend(currentEmail, friendEmail).subscribe();
    }
  }

  equals(string1: any, string2: any) {
    return string1 == string2
  }

  createChart(){

    var names:String[] = []
    var tipAmmount:Number[] = []

    for(let x of this.OwnTippsPerRound) {
      names.push(x.name)
      tipAmmount.push(x.betsList.length)
    }

    this.TippNumChart = new Chart("TippNumChart", {
      type: 'bar', //this denotes tha type of chart

      data: {// values on X-Axis
        labels: names,
        datasets: [
          {
            maxBarThickness: 50,
            label: "Tipps pro Runde",
            data: tipAmmount,
          },
        ]
      },
      options: {
        indexAxis: "y"
      }
    });
  }

  createPieChart(){

    if(this.PieChart)
      this.PieChart.destroy()

    var teams:String[] = []
    var points:Number[] = []

    Object.keys(this.PieData).forEach(key => {
      if(this.PieData[key] != 0) {
      teams.push(key)
      points.push(this.PieData[key])
      }})

    this.PieChart = new Chart("PieChart", {
      type: 'doughnut', //this denotes tha type of chart

      data: {// values on X-Axis
        labels: teams,
        datasets: [
          {
            label: "Points per Team",
            data: points,
          },
        ]
      },
      options: {
        aspectRatio:2
      }
    });
  }

  getOwnedTipprunden() {
    let userid = Number(sessionStorage.getItem('id'))
    this.tipprundenservice.getOwnTippsPerRound(userid).subscribe(res=>{
      this.OwnTippsPerRound = res;
      this.createChart()
    })
  }

  getPieData(bettingroundID:number) {
    let userid = Number(sessionStorage.getItem('id'))
    this.tipprundenservice.getPointsPerTeam(bettingroundID, userid).subscribe(res => {
      this.PieData = res;
      this.createPieChart();
    })
  }
}
