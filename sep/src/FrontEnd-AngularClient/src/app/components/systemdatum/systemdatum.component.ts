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
  aktuellesDatum!: SystemDatum | any

  constructor(private changeDateService: ChangeDateServiceService) { }

  changeDate(): void{

    this.changeDateService.changeDate(this.SystemDatum.datum).subscribe(data=> alert('Datum geändert'),error => alert('Systemdatum konnte nicht geändert werden'));
    this.getDatum();
  }
  getDatum() : void{
    this.changeDateService.getDate().subscribe(res=>{
      this.aktuellesDatum=res
    })

  }
  ngOnInit(): void {
    this.getDatum()
}
}
