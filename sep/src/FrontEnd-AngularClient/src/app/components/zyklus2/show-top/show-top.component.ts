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

  topBets: Array<User> = new Array<User>()
  topTeams: Leaguedata | any;


  constructor(private showTopService: ShowTopService) { }

  ngOnInit(): void {
    this.getTopUser()
    console.log(this.topBets)
    this.getTopTeam()
  }

  getTopUser() {
    let newUser1 = new User()
    newUser1.id = "1"
    newUser1.firstName = "A"
    newUser1.lastName = "a"

    let newUser2 = new User()
    newUser2.id = "2"
    newUser2.firstName = "B"
    newUser2.lastName = "b"

    let newUser3 = new User()
    newUser3.id = "3"
    newUser3.firstName = "C"
    newUser3.lastName = "c"

    this.topBets.push(newUser1)
    this.topBets.push(newUser2)
    this.topBets.push(newUser3)
  }

  getTopTeam() {
    this.showTopService.getTopTeams().subscribe(data => {
      this.topTeams = <Leaguedata[]>data
      console.log(this.topTeams)

    })
  }

}
