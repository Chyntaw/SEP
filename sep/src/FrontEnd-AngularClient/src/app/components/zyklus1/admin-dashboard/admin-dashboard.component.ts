import { Component, OnInit } from '@angular/core';
import {Router, ActivatedRoute, ParamMap} from "@angular/router";
import {Liga} from "../../../models/liga";


@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.css']
})
export class AdminDashboardComponent implements OnInit {

  id:String | any;
  liga: Liga[] | any;

  constructor(private router:Router, private route: ActivatedRoute) { }

  ngOnInit(): void {

    this.route.queryParams.subscribe(params => {
      this.liga = params["id"]
    })

    if(!sessionStorage.getItem("eMail")) {
      this.router.navigate(['login'])
      alert("Bitte melden Sie sich zunächst an.")
    }
  }

  logout() {
    sessionStorage.clear()
    this.router.navigate(['/login'])
  }

}
