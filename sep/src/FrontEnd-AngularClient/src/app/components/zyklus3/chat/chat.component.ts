import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import Pusher from 'pusher-js';
import {User} from "../../../models/roles/user/user";
import {BettingRound} from "../../../models/betting-round";
import {Table} from "../../../models/table";
import {GetUserServiceService} from "../../../services/getUserService.service";
import {FriendListService} from "../../../services/friend-list.service";
import {TipprundenserviceService} from "../../../services/tipprundenservice.service";
import {Router} from "@angular/router";
import {FreischaltungenService} from "../../../services/freischaltungen.service";
import {ChatserviceService} from "../../../services/chatservice.service";

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css'],
  styles: [`
      :host {
          position: fixed;
          top: 0;
          left: 0;
        right: 0;
      }
   `]
})
export class ChatComponent implements OnInit {

  me: User = new User();
  retrievedImage: string | any;
  searchInput!:any
  logo: any
  existsImage:boolean = false;
  friends: User [] | any;
  selectedFriend:User = new User();
  userToShow!: User | any
  searchUserMail: User = new User();
  retrieveResonse: any;
  friendsPictures: string[] = [];
  base64Data: any;
  keinBildVorhanden: string = "data:image/jpeg;base64,null";
  sendmessage:any;



  constructor(private getUserService: GetUserServiceService,
              private friendListService: FriendListService, private chatservice:ChatserviceService) {
  }

  ngOnInit(): void {
   this.getUser()
    this.showFriendList()
  }
  getUser(): void{
    this.me.firstName = String(sessionStorage.getItem("firstName"))
    this.me.lastName = String(sessionStorage.getItem("lastName"))
    this.me.eMail = String(sessionStorage.getItem("eMail"))
    this.me.id = String(sessionStorage.getItem("id"))
    this.getImage()

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
        //this.getPicsOfFriends()
      });
    }
  }

  @Output() newChatEvent = new EventEmitter<boolean>();
  closeChat(value:boolean){
    console.log(value)
    this.newChatEvent.emit(value)
  }
    saveSelectedFriend(friend:User){
      this.selectedFriend=friend;
      console.log(this.selectedFriend)
    }
    getMessages(){
      this.chatservice.getMessage(Number(this.me.id),Number(this.selectedFriend.id)).subscribe(res=>{
          console.log(res)
      })
    }
    sendMessage(){
    this.getUser()
      this.chatservice.sendMessages(Number(this.me.id),Number(this.selectedFriend.id), this.sendmessage).subscribe(res=>{
        console.log(res)
      })
    }

}
