import { Component, OnInit } from '@angular/core';
import {Leaguedata} from "../../models/leaguedata";
import {ShowleagueserviceService} from "../../services/showleagueservice.service";
import {Liga} from "../../models/liga";
import {HttpClient} from "@angular/common/http";
import {FormControl, FormGroup, Validator, Validators} from "@angular/forms";
import {UpdateleaguedataService} from "../../services/updateleaguedata.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-show-league-data',
  templateUrl: './show-league-data.component.html',
  styleUrls: ['./show-league-data.component.css']
})
export class ShowLeagueDataComponent implements OnInit {

  ligen: Liga [] | any;
  data: Leaguedata[] | any;
  leaguedatalist: Leaguedata[] | any ;
  leaguedata : Leaguedata=new Leaguedata();
  zeigeAktion:boolean = false;


  constructor(private showleagueservice: ShowleagueserviceService,private http:HttpClient, private updateleaguedataservice:UpdateleaguedataService,  private dashboardRouter:Router) {
  }

  ngOnInit(): void {
      let user_role = localStorage.getItem("role")
      if(user_role=='BASIC'){
        this.zeigeAktion=true;}

      let nuller: Leaguedata = {
      id: 0,
      matchDay: 0,
      player1: '',
      player2: '',
      result: '',
      date: ''
      }
    this.leaguedatalist=nuller; //Sonst undefined
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

//Formgroup erstellen mit passenden FormControls
  LeagueDataupdateform=new FormGroup({
    id:new FormControl(),
    matchDay:new FormControl(),
    player1:new FormControl(),
    player2:new FormControl(),
    result:new FormControl(),
    date:new FormControl(),

  });

  updateLeagueData(id: number){
    this.updateleaguedataservice.getLeagueData(id)
      .subscribe(
        data => {

          this.leaguedatalist=data

        },
        error => alert('Keine entsprechenden Daten vorhanden'));
  }


  update() {

    //neues objekt erstellt und aktuelle Daten aus dem Modal übergeben
    this.leaguedata = new Leaguedata();

    this.leaguedata.id = this.LeagueDataId?.value;
    this.leaguedata.matchDay = this.LeagueDataMatchday?.value;
    this.leaguedata.player1 = this.LeagueDataPlayer1?.value;
    this.leaguedata.player2 = this.LeagueDataPlayer2?.value;
    this.leaguedata.result = this.LeagueDataResult?.value;
    this.leaguedata.date = this.LeagueDataDate?.value;

    console.log(this.leaguedata)

    //Das erstellte Objekt  fürs Updaten ans Backend übergeben
    this.updateleaguedataservice.updateLeagueData(this.leaguedata.id,this.leaguedata).subscribe(
      data => {

       alert('Update erfolgreich')

      },
      error =>alert("Update war nicht erfolgreich"))
  }

//aktuelle Daten aus der Updateform/Modal bekommen
  get LeagueDataId(){
    return this.LeagueDataupdateform.get("id");
  }
  get LeagueDataMatchday(){
    return this.LeagueDataupdateform.get('matchDay');
  }
  get LeagueDataDate(){
    return this.LeagueDataupdateform.get('date');
  }
  get LeagueDataPlayer1(){
    return this.LeagueDataupdateform.get('player1');
  }
  get LeagueDataPlayer2(){
    return this.LeagueDataupdateform.get('player2');
  }
  get LeagueDataResult(){
    return this.LeagueDataupdateform.get('result');
  }

//Methode validateNo aus Fremdcode: Link siehe unten¹
  validateNo(e:any): boolean {
    const charCode = e.which ? e.which : e.keyCode;
    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
      return false
    }
    return true
  }


  checkRole(){
    let user_Role = localStorage.getItem('role')
  if(user_Role == "ADMIN") {
  this.dashboardRouter.navigate(['/admin-dashboard'])
  }
  else {
  this.dashboardRouter.navigate(['/dashboard'])
  }
  }
}

//Links: ¹https://stackoverflow.com/questions/41465542/angular2-input-field-to-accept-only-numbers
