import { Component, OnInit } from '@angular/core';
import {ShowTopService} from "../../../services/services/showTop.service";
import {Bets} from "../../../models/bets";
import {Leaguedata} from "../../../models/leaguedata";
import {User} from "../../../models/roles/user/user";
import {Liga} from "../../../models/liga";

@Component({
  selector: 'app-show-top',
  templateUrl: './show-top.component.html',
  styleUrls: ['./show-top.component.css']
})

export class ShowTopComponent implements OnInit {

  kvUser: {[key: string]: Array<User>} = {}
  kvTeam: {[key: string]: Array<Leaguedata>} = {}
  league_user: string[] = []
  users: User[] = []
  leagues: Liga[] = []
  leagueDatas: Leaguedata[] = []


  constructor(private showTopService: ShowTopService) { }

  ngOnInit(): void {
    this.getAllLeagues()

  }

  getAllLeagues() {
    this.showTopService.getAllLeagues().subscribe(data => {
      this.leagues = <Liga[]>data
      console.log(this.leagues)
    })
  }

  getTopUser(id: number) {
    this.showTopService.getTopUser(id).subscribe(data => {

      this.users = <User[]>data

    })
  }


  getTopTeam(id: number) {
    this.showTopService.getTopTeams(id).subscribe(data => {
      this.leagueDatas = <Leaguedata[]>data

    })
  }



}
