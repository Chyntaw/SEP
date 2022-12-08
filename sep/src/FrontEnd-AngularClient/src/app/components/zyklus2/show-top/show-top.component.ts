import { Component, OnInit } from '@angular/core';
import {ShowTopService} from "../../../services/services/showTop.service";
import {Bets} from "../../../models/bets";
import {Leaguedata} from "../../../models/leaguedata";
import {User} from "../../../models/roles/user/user";

@Component({
  selector: 'app-show-top',
  templateUrl: './show-top.component.html',
  styleUrls: ['./show-top.component.css']
})

export class ShowTopComponent implements OnInit {

  kvUser: {[key: string]: Array<User>} = {}
  kvTeam: {[key: string]: Array<Leaguedata>} = {}


  constructor(private showTopService: ShowTopService) { }

  ngOnInit(): void {
    this.getTopUser()
    this.getTopTeam()
  }

  getTopUser() {
    this.showTopService.getTopUser().subscribe(data => {
      console.log(data)
      let liganame = ""
      let x = this.kvUser
      console.log(data)
      data.forEach(function (liga)  {

        let liganame = String(Object.keys(liga)[0])
        let currentUsers:User[] = new Array()
        Object.values(liga)[0].forEach(function(MatchdayData: any)  {
          let currentUser = new User
          currentUser.firstName = String(Object.values(MatchdayData)[2])
          currentUser.code = String(Object.values(MatchdayData)[4])
          currentUsers.push(currentUser)

        })
        x[liganame] = currentUsers
      })
      this.kvUser = x
      console.log(this.kvUser)
    })
  }

  getTopTeam() {
    this.showTopService.getTopTeams().subscribe(data => {
      let liganame = ""
      let x = this.kvTeam
      console.log(data)/*
      data.forEach(function (liga)  {

        let liganame = String(Object.keys(liga)[0])
        let currentTeams:Leaguedata[] = new Array()
        Object.values(liga)[0].forEach(function(MatchdayData: any)  {
          let currentTeam = new Leaguedata()
          currentTeam.player1 = String(Object.values(MatchdayData)[2])
          currentTeam.result = String(Object.values(MatchdayData)[4])
          currentTeams.push(currentTeam)

        })
        x[liganame] = currentTeams
      })
      this.kvTeam = x
      console.log(this.kvTeam)*/
    })
  }



}
