import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {User} from "../../../models/roles/user/user";
import {GetUserServiceService} from "../../../services/getUserService.service";
import {FriendListService} from "../../../services/friend-list.service";
import {Router} from "@angular/router";
import {TipprundenserviceService} from "../../../services/tipprundenservice.service";
import {BettingRound} from "../../../models/betting-round";

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



  constructor(private getUserService: GetUserServiceService,
              private friendListService: FriendListService,
              private router: Router, private tipprundenservice: TipprundenserviceService) {
  }

  ngOnInit(): void {
    this.getUser()
    this.zeigeMeineTipprunden()
  }

  getUser(): void {
    this.me.firstName = String(sessionStorage.getItem("firstName"))
    this.me.lastName = String(sessionStorage.getItem("lastName"))
    this.me.eMail = String(sessionStorage.getItem("eMail"))



  }


  @Output() newGroupChatEvent = new EventEmitter<boolean>();
  closeGroupChat(value: boolean) {
    console.log(value)
    this.newGroupChatEvent.emit(value)
  }

  zeigeMeineTipprunden() {
   const CurrentUserID = Number(sessionStorage.getItem("id"))
    this.tipprundenservice.getRoundsbyUserID(CurrentUserID).subscribe(res => {
      this.mytiprounds = res;
    })

  }

}
