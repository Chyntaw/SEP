import { Component, OnInit } from '@angular/core';
import {ChangeDateServiceService} from "../../services/changeDateService.service";
import {SystemDatum} from "../../models/SystemDatum";

@Component({
  selector: 'app-systemdatum',
  templateUrl: './systemdatum.component.html',
  styleUrls: ['./systemdatum.component.css']
})
export class SystemdatumComponent implements OnInit {

  SystemDatum: SystemDatum = new SystemDatum();
  aktuellesDatum!: any;

  constructor(private changeDateService: ChangeDateServiceService) { }

  changeDate(): void{

    this.changeDateService.changeDate(this.SystemDatum.datum).subscribe(data=> alert('Datum geändert'),error => alert('Systemdatum konnte nicht geändert werden'));

  }
  getDatum(){

    this.changeDateService.getDate().subscribe(res=>{
        console.log(res)
      this.aktuellesDatum=res
      console.log(this.aktuellesDatum)
    })

  }
  ngOnInit(): void {
    this.changeDateService.getDate().subscribe(res=>{
      console.log(res)
      this.aktuellesDatum=res
      console.log(this.aktuellesDatum)



  })
}
}
