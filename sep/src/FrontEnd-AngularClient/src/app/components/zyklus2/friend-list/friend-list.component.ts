import { Component, OnInit } from '@angular/core';
import {User} from "../../../models/roles/user/user";
import {FriendListService} from "../../../services/friend-list.service";

@Component({
  selector: 'app-friend-list',
  templateUrl: './friend-list.component.html',
  styleUrls: ['./friend-list.component.css']
})
export class FriendListComponent implements OnInit {
  user:User = new User();
  friends: User [] | any;

  pendingFriends: User[] | any;
  pendingRequestedFriends: User[] | any;


  base64Data: any;
  retrieveResonse: any;
  keinBildVorhanden: string = "data:image/jpeg;base64,null";




  constructor(private friendListService: FriendListService) { }

  ngOnInit() {
    this.showFriendList();
    this.getUser();
    this.showPendingFriendList(this.user.eMail);
    this.showSendedFriendRequest(this.user.eMail);
  }

  showFriendList() {
    const email: string | null = sessionStorage.getItem('eMail');
    if (email) {
      this.friendListService.showFriends(email).subscribe(data => {
        this.friends = data;
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

  acceptFriend(friendEmail: string){
    const currentEmail: string | null = sessionStorage.getItem('eMail');
    if(currentEmail){
      console.log(friendEmail, currentEmail)
      this.friendListService.acceptFriend(currentEmail, friendEmail).subscribe(res=>{
        console.log("Res: " + res)
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

  getUser():void{
    const email: string|null = sessionStorage.getItem('eMail');
    if(email){
      this.user.eMail = email;
    }
  }


  equals(string1: any, string2: any) {
    return string1 == string2
  }





}
