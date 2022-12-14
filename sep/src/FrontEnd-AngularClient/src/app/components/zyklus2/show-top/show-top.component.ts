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

  async getTopUser(id: number) {
    this.showTopService.getTopUser(id).subscribe(data => {

      this.users = <User[]>data
      for(let user of this.users) {
        let help = Number(user.code)
        if(help < 0) {
          user.code = "0"
        }
      }
    })
    return this.users
  }


  async getTopTeam(id: number) {
    this.showTopService.getTopTeams(id).subscribe(data => {
      this.leagueDatas = <Leaguedata[]>data
      for (let league_data of this.leagueDatas) {
        let help = Number(league_data.result)
        if (help < 0) {
          league_data.result = "0"
        }
      }
    })
    return this.leagueDatas
  }

    //TODO: Erstelle nur eine Methode, die die ID entgegen nimmt und führe dort die beiden
    //      top Methoden aus. Prüfe danach, ob Punkte (>0) ausgegeben werden können
    //      Wenn ja: zeige diese an
    //      Wenn nein: Sag dem User, dass es keine gewonnen Punkte bisher gibt
    // ----> METHODE AUSFÜHREN

  async showData(id: number) {
    this.users = await this.getTopUser(id)
    this.leagueDatas = await this.getTopTeam(id)
    let pointsExist = true;
    for(let user of this.users) {
      if(user.code == "0") {
        console.log(user.code)
        pointsExist = false
      }
    }

    for(let leagueData of this.leagueDatas) {
      if(leagueData.result == "0") {
        pointsExist = false
      }
    }
  }

}

