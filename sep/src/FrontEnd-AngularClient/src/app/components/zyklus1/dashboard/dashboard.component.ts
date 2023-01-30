import {Component, EventEmitter, Input, OnInit} from '@angular/core';
import {Router} from "@angular/router";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

    showChat=false;
    showGroupChat=false;



  constructor(private router:Router) { }

  ngOnInit(): void {
    if(!sessionStorage.getItem("eMail")) {
      this.router.navigate(['login'])
      alert("Bitte melden Sie sich zunächst an. Und öffnen dann erst den Link")
    }
  }

  logout() {
    sessionStorage.clear()
    this.router.navigate(['/login'])
  }
  Chat(){
    this.showChat=true;
  }
  closeChat(val:boolean){
    this.showChat=val;
}
  closeGroupChat(val:boolean){
    this.showGroupChat=val;
  }
  GroupChat(){
    this.showGroupChat=true;
  }

}
