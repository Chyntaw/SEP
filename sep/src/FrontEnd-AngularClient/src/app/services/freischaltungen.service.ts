import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {User} from "../models/roles/user/user";

@Injectable({
  providedIn: 'root'
})
export class FreischaltungenService {

  private databaseURL="http://localhost:8080"

  constructor(private http:HttpClient) { }

  getAllAcceptedUsers(){
    return this.http.get<User[]>(`${this.databaseURL+'/user/listAcceptedUsers'}`);
  }

  getAllPendingUsers() {
    return this.http.get<User[]>(`${this.databaseURL+'/user/listPendingUsers'}`);
  }

  accept(eMail: string) {
    return this.http.get<User[]>(`${this.databaseURL+'/user/acceptUser/'+eMail}`);
  }

  decline(eMail: string) {
    return this.http.get<User[]>(`${this.databaseURL+'/user/declineUser/'+eMail}`);
  }

  isFreischaltungBeantragt(eMail:string){
    return this.http.get(`${this.databaseURL+"/user/isBeantragt/"+eMail}`)
  }
  beantrageFreischaltung(eMail:string){
    return this.http.get(`${this.databaseURL+"/user/beantrageFreischaltung/"+eMail}`)
  }

  isFreigeschaltet(eMail:string){
    return this.http.get(`${this.databaseURL+"/user/istFreigeschaltet/"+eMail}`)
  }
}
