import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {User} from "../../../models/roles/user/user";
import {GetUserServiceService} from "../../../services/getUserService.service";
import {FriendListService} from "../../../services/friend-list.service";
import {ChatserviceService} from "../../../services/chatservice.service";
import {Messages} from "../../../models/messages";

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
  friends: User [] | any;
  selectedFriend:User = new User();
  retrieveResonse: any;
  base64Data: any;
  keinBildVorhanden: string = "data:image/jpeg;base64,null";
  sendmessage:any;
  messages: Messages[] = new Array();
  currUser:any;
  intervalltimer:any;


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
    this.currUser= Number(sessionStorage.getItem("id"))
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
      });
    }
  }

  @Output() newChatEvent = new EventEmitter<boolean>();
  closeChat(value:boolean){
    console.log(value)
    this.newChatEvent.emit(value)
    this.ngOnDestroy()
  }



  saveSelectedFriend(friend:User){
    clearInterval(this.intervalltimer);
    this.selectedFriend=friend;
    console.log(this.selectedFriend)
    this.intervalltimer=setInterval(() => {
      this.getMessages();
    }, 5 * 1000);


  }
  getMessages(){
    this.chatservice.getMessage(Number(this.me.id),Number(this.selectedFriend.id)).subscribe(res=>{
      console.log(res)
      this.messages=res.messages;
    })
  }
  sendMessage(){
    this.getUser()
      if(this.selectedFriend.id){
        this.chatservice.sendMessages(Number(this.me.id),Number(this.selectedFriend.id), this.sendmessage).subscribe(res=>{
          console.log(res)
          this.sendmessage=null;
        })
      }
      else{
        alert("Wähle zuerst einen Freund aus!")
      }
    }

  ngOnDestroy() {
    clearInterval(this.intervalltimer);
  }
}


