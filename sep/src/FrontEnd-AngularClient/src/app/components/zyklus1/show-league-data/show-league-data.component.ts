import { Component, OnInit } from '@angular/core';
import {Leaguedata} from "../../../models/leaguedata";
import {ShowleagueserviceService} from "../../../services/showleagueservice.service";
import {Liga} from "../../../models/liga";
import {HttpClient} from "@angular/common/http";
import {FormControl, FormGroup, Validator, Validators} from "@angular/forms";
import {UpdateleaguedataService} from "../../../services/updateleaguedata.service";
import {Router} from "@angular/router";
import {TipprundenserviceService} from "../../../services/tipprundenservice.service";
import {BettingRound} from "../../../models/betting-round";
import {error} from "@angular/compiler-cli/src/transformers/util";
import {SystemDatum} from "../../../models/SystemDatum";
import {ChangeDateServiceService} from "../../../services/changeDateService.service";
import {Table} from "../../../models/table";

@Component({
  selector: 'app-show-league-data',
  templateUrl: './show-league-data.component.html',
  styleUrls: ['./show-league-data.component.css']
})
export class ShowLeagueDataComponent implements OnInit {

  ligen: Liga [] | any;
  ligenFuerTipprunde: Liga[] | any;
  data: Leaguedata[] | any;
  leaguedatalist: Leaguedata[] | any;
  leaguedata: Leaguedata = new Leaguedata();
  zeigeAktion: boolean = false;
  zeigeTippRundenErstellen: boolean = true;
  ligaid!: number;
  betRound: BettingRound = new BettingRound();
  passedLiga: boolean[] | any;
  aktuellesDatum: SystemDatum | any;
  mytiprounds: BettingRound[] | any;
  ligaNamen: string[] = [];
  tempLiga: Liga[] | any;

  LigaTable: Table[] | any





  constructor(private showleagueservice: ShowleagueserviceService,
              private http: HttpClient,
              private updateleaguedataservice: UpdateleaguedataService,
              private dashboardRouter: Router, private router: Router,
              private tipprundenService: TipprundenserviceService,
              private changeDateService: ChangeDateServiceService,
              private tipprundenservice: TipprundenserviceService) {
  }

  ngOnInit(): void {
    this.getDatum();
    this.zeigeMeineTipprunden();
    this.zeigeLigen();

    let user_role = sessionStorage.getItem("role")
    if (user_role == 'BASIC') {
      this.zeigeAktion = true;
    }

    if (user_role == sessionStorage.getItem("role"))
      if (user_role == 'ADMIN') {
        this.zeigeTippRundenErstellen = false;
      }
    let nuller: Leaguedata = {
      id: 0,
      matchDay: 0,
      player1: '',
      player2: '',
      result: '',
      date: ''
    }
    this.leaguedatalist = nuller; //Sonst undefined

  }

  zeigeLigen() {
    this.showleagueservice.findAll().subscribe(res => {
      this.ligen = res
      this.disableButtons();
    })

  }

  disableButtons() {
    this.showleagueservice.getDisabledButtons().subscribe(res => {
      this.ligenFuerTipprunde = res;
      console.log(this.ligenFuerTipprunde)
    })
  }
  logout() {
    sessionStorage.clear()
    this.router.navigate(['/login'])
  }
  zeigeLigaDaten(id: number) {
    this.showleagueservice.getAll(id).subscribe(res => {
      this.data = res
    })
  }

//Formgroup erstellen mit passenden FormControls
  LeagueDataupdateform = new FormGroup({
    id: new FormControl(),
    matchDay: new FormControl(),
    player1: new FormControl(),
    player2: new FormControl(),
    result: new FormControl(),
    date: new FormControl(),

  });

  updateLeagueData(id: number) {
    this.updateleaguedataservice.getLeagueData(id)
      .subscribe(
        data => {

          this.leaguedatalist = data

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

    //Das erstellte Objekt fürs Updaten ans Backend übergeben
    this.updateleaguedataservice.updateLeagueData(this.leaguedata.id, this.leaguedata).subscribe(
      data => {

        alert('Update erfolgreich')

      },
      error => alert("Update war nicht erfolgreich"))
  }

//aktuelle Daten aus der Updateform/Modal bekommen
  get LeagueDataId() {
    return this.LeagueDataupdateform.get("id");
  }

  get LeagueDataMatchday() {
    return this.LeagueDataupdateform.get('matchDay');
  }

  get LeagueDataDate() {
    return this.LeagueDataupdateform.get('date');
  }

  get LeagueDataPlayer1() {
    return this.LeagueDataupdateform.get('player1');
  }

  get LeagueDataPlayer2() {
    return this.LeagueDataupdateform.get('player2');
  }

  get LeagueDataResult() {
    return this.LeagueDataupdateform.get('result');
  }

//Methode validateNo aus Fremdcode: Link siehe unten¹
  validateNo(e: any): boolean {
    const charCode = e.which ? e.which : e.keyCode;
    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
      return false
    }
    return true
  }


  checkRole() {

    let user_Role = sessionStorage.getItem('role')
    if (user_Role == "ADMIN") {
      this.dashboardRouter.navigate(['/admin-dashboard'], {queryParams: {id: this.ligen}})
    } else {
      this.dashboardRouter.navigate(['/dashboard'])
    }
  }


  //Tipprunden
  sichereId(id: number) {

    this.ligaid = id;
    console.log(this.ligaid)

  }

  erstelleTipprunde(id: number) {


    let ownerID = Number(sessionStorage.getItem('id'))
    console.log(ownerID)
    this.betRound.ligaID = id;
    this.tipprundenService.createTipprunde(id, ownerID, this.betRound.name, this.betRound.isPrivate, this.betRound.corrScorePoints, this.betRound.corrGoalPoints, this.betRound.corrWinnerPoints, this.betRound.passwordTipprunde)
      .subscribe(res => {
          alert("Tipprunde erstellt")
          console.log(res)

        }, error => alert("Tipprunde konnte nicht erstellt werden")
      )
  }

  getDatum(): void {
    this.changeDateService.getDate().subscribe(res => {
      this.aktuellesDatum = res
    })
  }

  zeigeMeineTipprunden() {
    this.tipprundenservice.getRoundsbyUserID(Number(sessionStorage.getItem("id"))).subscribe(res => {
      this.mytiprounds = res;
      this.getNames();
    })


  }

  getNames() {
    if (this.mytiprounds) {
      for (let x of this.mytiprounds) {
        this.showleagueservice.getLigaName(x.ligaID).subscribe(res => {
          this.tempLiga = res;
          this.ligaNamen.push(this.tempLiga.name);
        })
      }
    }


  }

  getNext(curr:string) {
    return String(Number(curr)+1)
  }

  getTableByLigaID(id:number) {
    this.showleagueservice.getTableByLigaID(id).subscribe(res => {
      this.LigaTable = res;
    })
  }

}



//Links: ¹https://stackoverflow.com/questions/41465542/angular2-input-field-to-accept-only-numbers
