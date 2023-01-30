import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {User} from "../../../models/roles/user/user";
import {GetUserServiceService} from "../../../services/getUserService.service";
import {FriendListService} from "../../../services/friend-list.service";
import {Router} from "@angular/router";
import {TipprundenserviceService} from "../../../services/tipprundenservice.service";
import {BettingRound} from "../../../models/betting-round";
import {Messages} from "../../../models/messages";
import {ChatserviceService} from "../../../services/chatservice.service";

@Component({
  selector: 'app-groupchat',
  templateUrl: './groupchat.component.html',
  styleUrls: ['./groupchat.component.css'],
  styles: [`
      :host {
          position: fixed;
          top: 0;
          left: 0;
        right: 0;
      }
   `]
})
export class GroupchatComponent implements OnInit {

  me: User = new User();
  retrievedImage: string | any;
  searchInput!: any
  friends: User [] | any;
  mytiprounds: BettingRound[] | any;
  sendmessage:any;
  messages: Messages[] = new Array();
  currUser:any;
  intervalltimer:any;
  selectedTipround: BettingRound = new BettingRound();
  TiproundParticipant:User = new User();


  constructor(private getUserService: GetUserServiceService,
              private friendListService: FriendListService,
              private router: Router, private tipprundenservice: TipprundenserviceService, private chatservice:ChatserviceService) {
  }

  ngOnInit(): void {
    this.getUser()
    this.zeigeMeineTipprunden()
    this.intervalltimer=0
  }

  getUser(): void {
    this.me.firstName = String(sessionStorage.getItem("firstName"))
    this.me.lastName = String(sessionStorage.getItem("lastName"))
    this.me.eMail = String(sessionStorage.getItem("eMail"))
    this.currUser= Number(sessionStorage.getItem("id"))



  }


  @Output() newGroupChatEvent = new EventEmitter<boolean>();
  closeGroupChat(value: boolean) {
    this.ngOnDestroy()
    console.log(value)
    this.newGroupChatEvent.emit(value)
  }

  zeigeMeineTipprunden() {
   const CurrentUserID = Number(sessionStorage.getItem("id"))
    this.tipprundenservice.getRoundsbyUserID(CurrentUserID).subscribe(res => {
      this.mytiprounds = res;
    })

  }
  saveSelectedTipround(tipround:BettingRound){
    clearInterval(this.intervalltimer);
    this.selectedTipround=tipround;
    console.log(this.selectedTipround)
    this.intervalltimer=setInterval(() => {
      this.getMessages();
    }, 5 * 1000);

  }
  getMessages(){
    this.chatservice.getGroupChatMessage(this.currUser,Number(this.selectedTipround.id)).subscribe(res=>{
      console.log(res)
      this.messages=res.messages;

    })
  }
  sendMessage(){
    this.getUser()
    if(this.selectedTipround){
      this.chatservice.sendGroupChatMessages(this.currUser,Number(this.selectedTipround.id), this.sendmessage).subscribe(res=>{
        console.log(res)
        this.sendmessage=null;
      })
    }
    else{
      alert("Wähle zuerst eine Tipprunde aus!")
    }
  }

  ngOnDestroy() {
    clearInterval(this.intervalltimer);
  }
}
