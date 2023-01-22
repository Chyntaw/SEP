import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ChatserviceService {
  private databaseURL = 'http://localhost:8080';
  constructor(private httpClient: HttpClient) { }

//Einzelchat
  sendMessages(userID:number, friendID:number, message:string) {
    const formData =new FormData();
    formData.append('message', message)
    return this.httpClient.post(`${this.databaseURL+'/chat/postMessage/'+userID+'/'+friendID}`, formData)
  }
  getMessage(userID:number, friendID:number):Observable<any>{
    return this.httpClient.get(`${this.databaseURL+'/chat/getMessage/'+userID+'/'+friendID}`)
  }


//Gruppenchat
  sendGroupChatMessages(userID:number, tipprundenID:number, message:string) {
    const formData =new FormData();
    formData.append('message', message)
    return this.httpClient.post(`${this.databaseURL+'/chat/postGroupMessage/'+userID+'/'+tipprundenID}`, formData)
  }
  getGroupChatMessage(userID:number, tipprundenID:number):Observable<any>{
    console.log(userID)
    return this.httpClient.get(`${this.databaseURL+'/chat/getGroupMessage/'+userID+'/'+tipprundenID}`)
  }
}
