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

  constructor(private changeDateService: ChangeDateServiceService) { }

  changeDate(): void{

    this.changeDateService.changeDate(this.SystemDatum.datum).subscribe();



    console.log(this.SystemDatum);
    alert("Datum Geändert");
  }

  ngOnInit(): void {
  }
}
