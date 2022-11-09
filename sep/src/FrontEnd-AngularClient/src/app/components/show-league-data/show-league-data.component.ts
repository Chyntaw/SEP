import { Component, OnInit } from '@angular/core';
import {Leaguedata} from "../../models/leaguedata";
import {ShowleagueserviceService} from "../../services/showleagueservice.service";
import {Liga} from "../../models/liga";
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-show-league-data',
  templateUrl: './show-league-data.component.html',
  styleUrls: ['./show-league-data.component.css']
})
export class ShowLeagueDataComponent implements OnInit {

  data: Leaguedata[] | any;
  ligen: Liga [] | any;

  constructor(private showleagueservice: ShowleagueserviceService,private http:HttpClient) {
  }

  ngOnInit(): void {
  this.zeigeLigen();

  }

  zeigeLigen() {
    this.showleagueservice.findAll().subscribe(res => {
      this.ligen = res




    })
  }

  zeigeLigaDaten(id:number) {

    this.showleagueservice.getAll(id).subscribe(res => {
      this.data = res


    })

  }
}
