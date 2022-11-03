import { NgModule } from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import { CommonModule } from '@angular/common';
import {LoginComponent} from "./components/login/login.component";
import {RegistrationComponent} from "./components/registration/registration.component";
import {ZweiFAComponent} from "./components/zwei-fa/zwei-fa.component";
import {DashboardComponent} from "./components/dashboard/dashboard.component";

const MeineRouten : Routes = [
  {path:'login', component: LoginComponent},
  {path:'registration', component:  RegistrationComponent},
  {path: 'zwei-fa', component: ZweiFAComponent},
  {path: 'dashboard', component: DashboardComponent},
  {path: '',   redirectTo: '/login', pathMatch: 'full'}
];

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    RouterModule,
    RouterModule.forRoot(MeineRouten)

  ]
})
export class AppRoutingModule { }
