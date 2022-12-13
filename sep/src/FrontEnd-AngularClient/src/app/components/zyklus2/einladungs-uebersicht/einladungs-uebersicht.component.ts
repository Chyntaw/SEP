import {APP_BOOTSTRAP_LISTENER, Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {User} from "../../../models/roles/user/user";
import {LoginserviceService} from "../../../services/loginservice.service";
import {BettingRound} from "../../../models/betting-round";
import {TipprundenserviceService} from "../../../services/tipprundenservice.service";
import {Score} from "../../../models/score";
import {GetUserServiceService} from "../../../services/getUserService.service";
import {ShowleagueserviceService} from "../../../services/showleagueservice.service";
import {Liga} from "../../../models/liga";

@Component({
  selector: 'app-einladungs-uebersicht',
  templateUrl: './einladungs-uebersicht.component.html',
  styleUrls: ['./einladungs-uebersicht.component.css']
})
export class EinladungsUebersichtComponent implements OnInit {

  rundenID!:any;
  user:User=new User;
  displayStyle = "none";
  currBettingRound: BettingRound = new BettingRound();
  participants!: User[];
  scoreslist!: any[];
  leaderboard!: Score[] | any;
  OwnerID!:number;
  Owner:User = new User;
  currLiga:Liga= new Liga();
  currBettingRoundPassword!:string;
  retrievedImage: string | any;
  base64Data: string | any;
  retrieveResonse: string | any;
  alias!:string;

  constructor(private router:Router, private activatedRoute:ActivatedRoute, private loginserviceService: LoginserviceService,
              private tipprundenservice:TipprundenserviceService,private getuserservice:GetUserServiceService,
              private leaguedataservice: ShowleagueserviceService) {}

  ngOnInit(): void {

    if(!sessionStorage.getItem("eMail") || sessionStorage.getItem("eMail")==null) {
      this.openPopup();
    }
    this.rundenID =this.activatedRoute.snapshot.paramMap.get("id")
    console.log(this.rundenID)
    this.getTipprundeByID()
    this.getLeaderBoard()
  }

  logout() {
    sessionStorage.clear()
    this.router.navigate(['/login'])
  }
  userLogin() {
    console.log(this.user)
    this.loginserviceService.loginUser2(this.user.eMail,this.user.password).subscribe(data=>{
      this.getTipprundeByID()
      console.log(data)
      const data_str = JSON.stringify(data);
      const jsondata = JSON.parse(data_str)

      sessionStorage.setItem("id", jsondata['id']);
      sessionStorage.setItem("eMail", jsondata['eMail']);
      sessionStorage.setItem("firstName", jsondata['firstName']);
      sessionStorage.setItem("lastName", jsondata['lastName']);
      sessionStorage.setItem("password", jsondata['password']);
      sessionStorage.setItem("role", jsondata['role'])
     this.closePopup()

    },error=>alert("Bitte überprüfen Sie Ihre Eingaben!"));
  }

  openPopup() {
    this.displayStyle = "block";
  }
  closePopup() {
    this.displayStyle = "none";
  }
  getTipprundeByID(){
    this.tipprundenservice.getTipproundByID(this.rundenID).subscribe(res=>{
      console.log(res)
      this.participants=res.participants
      this.scoreslist=res.scoresList
      console.log(this.scoreslist)
      console.log(this.participants)
        this.currBettingRound=res
      this.getOwnerByID(this.OwnerID=res.ownerID)
      this.getLigaByID(res.ligaID)
      this.currBettingRoundPassword=res.password

    })
  }
  getLeaderBoard() {
    this.tipprundenservice.getLeaderBoard(this.rundenID).subscribe(res => {
      console.log(res)
      this.leaderboard = res;
      console.log(this.leaderboard)
    })

  }
  getOwnerByID(ownerID:number){
    this.getuserservice.getUserByID(ownerID).subscribe(res=>{
        this.Owner=res;
        console.log(res)
      this.getImage()
    })
  }
  getLigaByID(ligaID:number){
    this.leaguedataservice.findLigaByID(ligaID).subscribe(res=>{
      this.currLiga=res;
    })
  }
  getImage(){
    this.getuserservice.getImage(this.Owner.eMail).subscribe(res=>{
      console.log(res);
      this.retrieveResonse = res;
      if(this.retrieveResonse.picByte != null) {
        this.base64Data = this.retrieveResonse.picByte;
        this.retrievedImage = 'data:image/jpeg;base64,' + this.base64Data;
      }
    })}
  tipprundeBeitreten(){
    let userid = Number(sessionStorage.getItem("id"))
    if(this.alias != null){
    this.tipprundenservice.addAlias(this.alias,userid,this.currBettingRound.id).subscribe(res=>{

    })}

    this.tipprundenservice.addParticipant(userid,this.currBettingRound.id,this.currBettingRoundPassword).subscribe(res=>{
      alert("Beitritt erfolgreich")

    },error =>alert("Beitritt nicht erfolgreich"))



  }
}
