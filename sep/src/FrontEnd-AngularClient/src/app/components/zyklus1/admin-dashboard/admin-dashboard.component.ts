import { Component, OnInit } from '@angular/core';
import {Router} from "@angular/router";

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.css']
})
export class AdminDashboardComponent implements OnInit {

  constructor(private router:Router) { }

  ngOnInit(): void {
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
