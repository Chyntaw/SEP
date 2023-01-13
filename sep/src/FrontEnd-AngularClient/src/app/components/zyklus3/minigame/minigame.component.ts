import { Component, OnInit } from '@angular/core';
import {Router} from "@angular/router";
import {GetUserServiceService} from "../../../services/getUserService.service";
import {User} from "../../../models/roles/user/user";

@Component({
  selector: 'app-minigame',
  templateUrl: './minigame.component.html',
  styleUrls: ['./minigame.component.css']
})
export class MinigameComponent implements OnInit {
  AbstandLinks="auto";
  AbstandRechts="auto";
  AbstandOben="auto";
  AbstandUnten="auto";
  Anzeigen="block";
  BildAnzeigen="none";
  money:any;
  me: User = new User();


  constructor(private router:Router,private getUserService: GetUserServiceService) { }

  ngOnInit(): void {
    this.getUser()
    this.getMoney()
  }
  getUser(): void {

    this.me.firstName = String(sessionStorage.getItem("firstName"))
    this.me.lastName = String(sessionStorage.getItem("lastName"))
    this.me.eMail = String(sessionStorage.getItem("eMail"))
  }
  miniGameStarten(){
    this.changePosition()
    setInterval(() => {
      this.changePosition();
    }, 10 * 1000);
  }

  changePosition(){
    this.BildAnzeigen="block"
    this.Anzeigen="block";
    this.AbstandLinks= Math.floor((Math.random() * 1000) + 1)+"px"
    this.AbstandRechts=Math.floor((Math.random() * 1000) + 1)+"px"
    this.AbstandOben=Math.floor((Math.random() * 500) + 1)+"px"
    this.AbstandUnten=Math.floor((Math.random() * 500) + 1)+"px"
    setTimeout(() => {
      this.hideButton();
    }, 5 * 1000);
  }

  hideButton(){
    this.Anzeigen="none";

  }
  logout() {
    sessionStorage.clear()
    this.router.navigate(['/login'])
  }
  miniGameStoppen(){
    window.location.reload()
  }
  getMoney(){
    this.getUserService.getMoney(this.me.eMail).subscribe(res=>{

      this.money = parseFloat(res.toString());
    })
  }
  increaseMoney(){
    this.money=this.money+20
    this.getUserService.increaseMoney(this.me.eMail).subscribe(res=>{
      console.log(res)

    })
}
}

