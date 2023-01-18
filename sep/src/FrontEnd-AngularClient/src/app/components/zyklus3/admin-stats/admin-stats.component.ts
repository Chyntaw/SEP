import { Component, OnInit } from '@angular/core';
import {Liga} from "../../../models/liga";
import {AdminStatsService} from "../../../services/services/admin-stats.service";

@Component({
  selector: 'app-admin-stats',
  templateUrl: './admin-stats.component.html',
  styleUrls: ['./admin-stats.component.css']
})
export class AdminStatsComponent implements OnInit {

  leagueBettingRounds:Liga[] = []
  leagueUsers:Liga[] = []

  constructor(private adminStatsService: AdminStatsService) { }

  ngOnInit(): void {
    this.sortLeaguesByBettingRound()
    this.sortLeaguesByUser()
  }

  sortLeaguesByBettingRound() {
    this.adminStatsService.getLeaguesSortedByBettingRound().subscribe(data => {

      let object = <any>data
      let json = JSON.stringify(object)
      json = json.substring(1, json.length - 1)
      let temp = json.split(",")

      let values = []
      for(let i=0, j=0; i<temp.length; i+=2, j++) {
        values[j] = temp[i] + "," + temp[i+1]
      }

      for(let i = 0; i<values.length; i++) {

        // id and name
        let help = values[i].split(":")[0].split(", ")
        help[0] = help[0].substring(2)
        help[1] = help[1].substring(0, help[1].length-2)

        // score
        let score = parseInt(values[i].split(":")[1])

        // set values
        let newLiga = new Liga()
        newLiga.id = parseInt(help[0])
        newLiga.name = help[1]
        newLiga.score = score

        this.leagueBettingRounds.push(newLiga)



      }

      this.leagueBettingRounds = this.leagueBettingRounds.sort(({score:a}, {score:b}) => b-a)

    })
  }

  sortLeaguesByUser() {
    this.adminStatsService.getLeaguesSortedByUsers().subscribe(data => {
      let object = <any>data
      let json = JSON.stringify(object)
      json = json.substring(1, json.length - 1)
      let temp = json.split(",")

      let values = []
      for(let i=0, j=0; i<temp.length; i+=2, j++) {
        values[j] = temp[i] + "," + temp[i+1]
      }


      for(let i = 0; i<values.length; i++) {

        // id and name
        let help = values[i].split(":")[0].split(", ")
        help[0] = help[0].substring(2)
        help[1] = help[1].substring(0, help[1].length-2)

        // score
        let score = parseInt(values[i].split(":")[1])

        // set values
        let newLiga = new Liga()
        newLiga.id = parseInt(help[0])
        newLiga.name = help[1]
        newLiga.score = score

        this.leagueUsers.push(newLiga)

      }
      this.leagueUsers = this.leagueUsers.sort(({score:a}, {score:b}) => b-a)

    })
  }

  resetCounterBR(id: number) {
    console.log("Wird zurückgesetzt")
    this.adminStatsService.putByBettingRound(id).subscribe(data => {
      console.log(data)
    })
    window.location.reload()
  }

  resetAllBR() {
    for(let i = 0; i < this.leagueBettingRounds.length; i++) {
      this.adminStatsService.putByBettingRound(this.leagueBettingRounds[i].id).subscribe(data => {
        console.log(data)
      })
    }
  }

  resetCounterB(id: number) {
    console.log("Du kommst voran!")
    this.adminStatsService.putByBet(id).subscribe(data => {
      console.log(data)
    })
    window.location.reload()
  }

  resetAllB() {
    for(let i = 0; i < this.leagueUsers.length; i++) {
      this.adminStatsService.putByBet(this.leagueUsers[i].id).subscribe(data => {
        console.log(data)
      })
    }
  }

}
