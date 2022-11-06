import { NgModule } from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import { CommonModule } from '@angular/common';
import {LoginComponent} from "./components/login/login.component";
import {RegistrationComponent} from "./components/registration/registration.component";
import {ZweiFaComponent} from "./components/zwei-fa/zwei-fa.component";
import {DashboardComponent} from "./components/dashboard/dashboard.component";
import {AdminRegistrationComponent} from "./components/admin-registration/admin-registration.component";


const MeineRouten : Routes = [
  {path:'login', component: LoginComponent},
  {path:'registration', component:  RegistrationComponent},
  {path: 'zwei-fa', component: ZweiFaComponent},
  {path: 'dashboard', component: DashboardComponent},
  {path: 'admin-registration', component: AdminRegistrationComponent},
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
