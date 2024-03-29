import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {User} from "../models/roles/user/user";


@Injectable({
  providedIn: 'root'
})
export class GetUserServiceService{
  private databaseURL = 'http://localhost:8080';
  constructor(private httpClient: HttpClient) {
  }


  getUser(email: string):Observable<User>{

    const formData: FormData = new FormData();
    formData.append('email', email);


    return this.httpClient.post<User>(`${this.databaseURL}/user/getUser`, formData);
  }

  addUser(eMail1: string, eMail2: string) {

    const formData: FormData = new FormData();
    formData.append('emailUser1', eMail1);
    formData.append('emailUser2', eMail2);

    return this.httpClient.post(`${this.databaseURL}/user/addFriend`, formData);
  }

  getImage(eMail: string) {
    return this.httpClient.get(`${this.databaseURL+'/user/'+eMail+'/image'}`)
  }
  getUserByID(userid:number):Observable<any>{
    console.log(userid)
    return this.httpClient.get(`${this.databaseURL+"/user/getUserbyID/"+userid}`)
  }

  isOldEnough(eMail: string) {
    return this.httpClient.get(`${this.databaseURL+"/user/isOldEnough/"+eMail}`)
  }


  getMoney(eMail: string) {
    return this.httpClient.get(`${this.databaseURL+"/user/getMoney/"+eMail}`)
  }

  increaseMoney(eMail:string) {
    console.log(eMail)
    return this.httpClient.post(`${this.databaseURL+"/user/increaseMoney/"+eMail}`,eMail)
  }
}
