import { Component, OnInit } from '@angular/core';
import {User} from "../../../models/roles/user/user";
import {FreischaltungenService} from "../../../services/freischaltungen.service";

@Component({
  selector: 'app-freischaltungen',
  templateUrl: './freischaltungen.component.html',
  styleUrls: ['./freischaltungen.component.css']
})
export class FreischaltungenComponent implements OnInit {

  acceptUsers:User[]|any

  userList:User[]|any


  constructor(private freischaltungsService: FreischaltungenService) { }

  ngOnInit(): void {
    this.getAllAcceptedUsers()
    this.getAllPendingUsers();
  }

  getAllAcceptedUsers(){
    this.freischaltungsService.getAllAcceptedUsers().subscribe(res=>{
      this.acceptUsers = res;
    })
  }

  getAllPendingUsers(){
    this.freischaltungsService.getAllPendingUsers().subscribe(res=>{
      this.userList = res;
    })
  }

  accept(eMail:string){
    this.freischaltungsService.accept(eMail).subscribe()
    window.location.reload()
  }
  decline(eMail:string){
    this.freischaltungsService.decline(eMail).subscribe()
    window.location.reload()
  }

}
