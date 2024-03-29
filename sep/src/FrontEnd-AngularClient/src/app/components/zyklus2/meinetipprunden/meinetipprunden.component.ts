import { Component, OnInit } from '@angular/core';
import {Leaguedata} from "../../../models/leaguedata";
import {ShowleagueserviceService} from "../../../services/showleagueservice.service";
import { FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {BettingRound} from "../../../models/betting-round";
import {Bets} from "../../../models/bets";
import {TipprundenserviceService} from "../../../services/tipprundenservice.service";
import {Score} from "../../../models/score";
import {FriendListService} from "../../../services/friend-list.service";
import {User} from "../../../models/roles/user/user";
import {ActivatedRoute, Router} from "@angular/router";
import {UpdateleaguedataService} from "../../../services/updateleaguedata.service";
import {SystemDatum} from "../../../models/SystemDatum";
import {ChangeDateServiceService} from "../../../services/changeDateService.service";

@Component({
  selector: 'app-meinetipprunden',
  templateUrl: './meinetipprunden.component.html',
  styleUrls: ['./meinetipprunden.component.css']
})
export class MeinetipprundenComponent implements OnInit {

  userid = Number(sessionStorage.getItem('id'));
  mytiprounds: BettingRound[] | any;
  matchDayCount:number [] = new Array;
  matchDayDaten:Leaguedata[] | any;
  Bets: String[] | any;
  _ligaID!:number;
  tipRundenID!: number;
  newBet:Bets = new Bets();
  selectedBettingRoundID: number | any;
  myTippRundenByLigaID: BettingRound[] | any;
  leaderboard!: Score[] | any;
  alias!: string;
  passedGame!:boolean[] | any;
  selectedUser: string | undefined;
  selectedFriend: string | undefined;
  allUser: User[] | any;
  ArrayWithTiproundsOwnerIDS: Number[] = [];
  CurrentUserID!: number;
  allFriends:User[]|any;
  currTipprunde:BettingRound=new BettingRound();
  aktuellesDatum: SystemDatum | any;


  constructor(private tipprundenservice: TipprundenserviceService, private showleaguedataservice: ShowleagueserviceService,
              private fb: FormBuilder, private friendListService: FriendListService, private router: Router,
              private updateleaguedataservice:UpdateleaguedataService, private changeDateService: ChangeDateServiceService) {
  }

  ngOnInit(): void {

    this.zeigeMeineTipprunden()
    this.findAllUser()
    this.findAllFriends()
    this.CurrentUserID = Number(sessionStorage.getItem("id"))
    this.getOwnedTipprunden()
    this.getDatum()


  }

  tippPattern = '^[0-9]+[-][0-9]+$';
  TippAbgabeForm = new FormGroup({
    tippAbgabe: new FormControl('', Validators.compose([
      Validators.required,
      Validators.pattern(this.tippPattern)]))
  })

  get f() {
    return this.TippAbgabeForm.controls;
  }

  get tippAbgabeWert() {
    return this.TippAbgabeForm.get("tippAbgabe")
  }

  FormControlReset() {
    this.TippAbgabeForm.controls.tippAbgabe.setValue(null)
  }

  getOwnedTipprunden() {
    let userid = Number(sessionStorage.getItem('id'))
    this.tipprundenservice.getOwnedTipprunden(userid).subscribe(res=>{
      for(var value of res){
        this.ArrayWithTiproundsOwnerIDS.push(value.ownerID)
      }
    })
  }

  getTippRundenByLigaID(bettingroundID: number) {
    this.tipprundenservice.getTipprundeByLigaID(bettingroundID, this.userid).subscribe(res => {
        this.myTippRundenByLigaID = res;
        this.selectedBettingRoundID = bettingroundID;
      }
    )
  }

  zeigeMeineTipprunden() {
    this.tipprundenservice.getRoundsbyUserID(this.userid).subscribe(res => {
      this.mytiprounds = res;
    })


  }

  ArrayFüllen() {
    for (let i = 1; i < this.matchDayCount.length; i++) {
      this.matchDayCount[i] = i;
    }
  }

  SaveligaID(ligaID: number, tippRundenid: number) {
    this._ligaID = ligaID;
    this.tipRundenID = tippRundenid;
  }
  MatchDayDaten(matchDayID:number){
    this.showleaguedataservice.getAllMatchDayDaten(this._ligaID,matchDayID).subscribe(res=>{

      this.matchDayDaten=res;
      let arr1 : Number[] = [];
      let dateArr: string[] = [];
      for(var val of this.matchDayDaten) {
        arr1.push(val.id);
        dateArr.push(val.date);
      }
      console.log(dateArr);
      this.getDisabled(dateArr);
      this.getBets(this.userid, arr1, this.tipRundenID)
    })
  }

  getBets(userID: number, leagueDataIDs: Number[], bettingRoundID: number) {
    this.tipprundenservice.getBetsByLeagueDataID(userID, leagueDataIDs, bettingRoundID).subscribe(res => {
      this.Bets = res;
    })
  }

// show TipHelp
  getTipHelp(id: number) {
    this.tipprundenservice.getTipHelpByTeams(id).subscribe(data => {
      let leaguedataResult = <Leaguedata>data
      if ("N/A" == leaguedataResult.result) {
        alert("Keine Tipphilfe derzeit verfügbar")
      } else {
        alert("Unser Tipp: " + leaguedataResult.result)
      }
    })
  }

  placeBet(leagueDataid: number, index: number) {
    this.tipprundenservice.placeBet(this.tipRundenID, this.userid, leagueDataid, this.matchDayDaten[index].newBet).subscribe(res => {
      alert("Tipp gespeichert!")
    }, error => alert("Tipp konnte nicht gespeichert werden"));
  }

  validateNo(e: any): boolean {
    const charCode = e.which ? e.which : e.keyCode;
    if (charCode == 45) return true;
    else if (charCode > 31 && (charCode < 48 || charCode > 57)) {
      return false
    }
    return true
  }

  transferBets(toTipprundenID: number) {
    this.tipprundenservice.transferBets(this.selectedBettingRoundID, toTipprundenID, this.userid).subscribe(res => {
      alert("Tipps übernommen!")
    }, error => alert("Fehler beim übernehmen der Tipps"));
  }

  getLeaderBoard(tipprundenID: number) {

    this.tipprundenservice.getLeaderBoard(tipprundenID).subscribe(res => {
      console.log(res)
      this.leaderboard = res;
      console.log(this.leaderboard)
    })

  }

  onSelectedUser(selectedUserEmail: string) {
    this.selectedUser = selectedUserEmail;
  }

  onSelectedFriend(selectedFriendEmail: string) {
    this.selectedFriend = selectedFriendEmail;

  }

  tipprundeEinladen(bettingroundid: number) {
    const userEmail = sessionStorage.getItem("eMail")
    if (userEmail && this.selectedUser) {
      this.friendListService.tipprundeEinladen(bettingroundid, userEmail, this.selectedUser).subscribe(res=>{
        alert("Nutzer wurde eingeladen!")
      }, error => alert("Einladung fehlgeschlagen"))
    }
  }

  teileTipps(bettingroundid: number){
    const userEmail = sessionStorage.getItem("eMail")
    if(userEmail && this.selectedFriend){
      this.friendListService.teileTips(bettingroundid, userEmail, this.selectedFriend).subscribe(res=>{
        alert("Tipps wurden geteilt!")
      }, error => alert("Tipp konnten nicht geteilt werden!"))
    }
  }

  findAllUser() {
    this.friendListService.findAllUser().subscribe(res => {
      this.allUser = res;
    })
  }
  findAllFriends() {
    let currentEmail = sessionStorage.getItem("eMail")
    if(currentEmail){
      this.friendListService.showFriends(currentEmail).subscribe(res => {
        this.allFriends = res;
      })
    }
  }
  AddAlias(bettingroundid: number) {

    this.tipprundenservice.addAlias(this.alias, this.CurrentUserID, bettingroundid).subscribe(res => {

    })

  }
  getMachDateBoolean(matchDate:string){
    console.log(matchDate)
    this.tipprundenservice.getMachDateBoolean(matchDate).subscribe(res=>{
        console.log(res)
    })
  }

  showProfile(eMail:string){
    this.router.navigate(['/getUser'], {queryParams: {eMail: eMail}})
  }
  getDisabled(date:string[]) {
    this.tipprundenservice.getDisabled(date).subscribe(res => {
      this.passedGame = res;
      console.log(res);
    });
  }
  getMatchDays(ligaID:number){
    this.updateleaguedataservice.getMatchDays(ligaID).subscribe(res=>{
      console.log(res)
      this.matchDayCount.length=res.length+1;
      this.ArrayFüllen()
    })
  }
  getTipprundeByID(currTipprundeID:number){
    this.tipprundenservice.getTipproundByID(currTipprundeID).subscribe(res=>{
       this.currTipprunde=res;
    })
  }
  logout() {
    sessionStorage.clear()
    this.router.navigate(['/login'])
  }
  getDatum(): void {
    this.changeDateService.getDate().subscribe(res => {
      this.aktuellesDatum = res
    })
  }
}
