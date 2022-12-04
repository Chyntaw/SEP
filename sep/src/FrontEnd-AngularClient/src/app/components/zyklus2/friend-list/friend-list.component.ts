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

  retrievedImage: any;
  base64Data: any;
  retrieveResonse: any;

  constructor(private friendListService: FriendListService) { }

  ngOnInit() {
    this.showFriendList();
    //this.getFriendImages();
    this.getUser();
  }

  getImage(eMail: string){
//    const eMail = sessionStorage.getItem('eMail')
    if(eMail){
      this.friendListService.getImagesromFriend(eMail).subscribe(res=>{
        if(res != null){
          this.retrieveResonse = res;
          this.base64Data = this.retrieveResonse.picByte;
          this.retrievedImage = 'data:image/jpeg;base64,' + this.base64Data;
        }
        else{
          this.retrievedImage = false;
        }
      })
    }
  }


  showFriendList() {
    const email:string | null = sessionStorage.getItem('eMail');

    if(email){
      this.friendListService.showFriends(email).subscribe(data=>{
        console.log(data)
          this.friends = data;
      })
    }
  }



  showPendingFriendList(email: string){
    this.friendListService.showPendingFriends(email).subscribe(data=>{
      this.pendingFriends = data;
      console.log(data)
    });
  }

  showSendedFriendRequest(email: string){
    this.friendListService.showPendingFriendRequests(email).subscribe(data=>{
      this.pendingRequestedFriends = data;

   //   console.log('data:image/jpeg;base64,' + this.pendingRequestedFriends[0].image.picByte)

    });
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


}
