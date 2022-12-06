import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {User} from "../models/roles/user/user";

@Injectable({
  providedIn: 'root'
})
export class FriendListService {

  private databaseURL="http://localhost:8080"


  constructor(private http:HttpClient) { }

  showFriends(email: string): Observable<User[]> {

    return this.http.get<User[]>(`${this.databaseURL+'/user/listFriends/'+email}`);
  }

  showPendingFriends(email: string){
    return this.http.get<User[]>(`${this.databaseURL+'/user/listPendingFriends/'+email}`);
  }
  showPendingFriendRequests(email: string){
    return this.http.get<User[]>(`${this.databaseURL+'/user/listPendingRequestedFriends/'+email}`);
  }

  acceptFriend(email: string, email2: string):Observable<any>{

    const formData: FormData = new FormData();

    formData.append('currentEmail', email);
    formData.append('friendEmail', email2)

    console.log("Service: AcceptFriend")

    return this.http.post(`${this.databaseURL+'/user/acceptFriend'}`, formData);
  }


  declineFriend(currentEmail: string, friendEmail: string) {
    const formData: FormData = new FormData();

    formData.append('currentEmail', currentEmail);
    formData.append('friendEmail', friendEmail);

    console.log("Service: DeclineFriend")


    return this.http.post(`${this.databaseURL+'/user/declineFriend'}`, formData);
  }

  removeFriend(currentEmail: string, friendEmail: string) {

    const formData: FormData = new FormData();

    formData.append('currentEmail', currentEmail);
    formData.append('friendEmail', friendEmail);


    return this.http.post(`${this.databaseURL+'/user/deleteFriend'}`, formData);

  }

  getImagesFromFriend(eMail: string) {
    return this.http.get(`${this.databaseURL+'/user/'+eMail+'/image'}`)
  }


  tipprundeEinladen(bettingroundid:number, currentEmail: string, friendEmail: string):Observable<any>{
    console.log(currentEmail + " " + friendEmail + " " + bettingroundid)
    return this.http.get(`${this.databaseURL+'/user/inviteFriend/'+currentEmail+'/'+friendEmail+'/'+bettingroundid}`)
  }

  findAllUser() {
    return this.http.get(`${this.databaseURL+'/user/findall'}`)
  }
}

