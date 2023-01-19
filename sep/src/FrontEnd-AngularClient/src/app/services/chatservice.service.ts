import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ChatserviceService {

  private databaseURL = 'http://localhost:8080';

  constructor(private httpClient: HttpClient) { }


  sendMessages(userID:number, friendID:number,message:string) {
    console.log(userID,friendID,message)
    return this.httpClient.get(`${this.databaseURL+'/chat/postMessage/'+userID+'/'+friendID+'/'+message}`)
  }
  getMessage(userID:number, friendID:number):Observable<any>{
    return this.httpClient.get(`${this.databaseURL+'/chat/getMessage/'+userID+'/'+friendID}`)
    //brauche die Info von wem jeweils welche Message ist

  }
}
