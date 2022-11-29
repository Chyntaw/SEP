import { NgModule } from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import { CommonModule } from '@angular/common';
import {LoginComponent} from "./components/zyklus1/login/login.component";
import {RegistrationComponent} from "./components/zyklus1/registration/registration.component";
import {ZweiFaComponent} from "./components/zyklus1/zwei-fa/zwei-fa.component";
import {DashboardComponent} from "./components/zyklus1/dashboard/dashboard.component";
import {AdminRegistrationComponent} from "./components/zyklus1/admin-registration/admin-registration.component";
import {AdminDashboardComponent} from "./components/zyklus1/admin-dashboard/admin-dashboard.component";
import {CreateLeagueComponent} from "./components/zyklus1/create-league/create-league.component";
import {SystemdatumComponent} from "./components/zyklus1/systemdatum/systemdatum.component";
import {ShowLeagueDataComponent} from "./components/zyklus1/show-league-data/show-league-data.component";
import {MeinetipprundenComponent} from "./components/zyklus2/meinetipprunden/meinetipprunden.component";
import {TipprundenuebersichtComponent} from "./components/zyklus2/tipprundenuebersicht/tipprundenuebersicht.component";


const MeineRouten : Routes = [
  {path:'login', component: LoginComponent},
  {path:'registration', component:  RegistrationComponent},
  {path: 'zwei-fa', component: ZweiFaComponent},
  {path: 'dashboard', component: DashboardComponent},
  {path: 'admin-registration', component: AdminRegistrationComponent},
  {path: 'admin-dashboard', component: AdminDashboardComponent},
  {path: 'create_league', component: CreateLeagueComponent},
  {path: 'systemdatum', component: SystemdatumComponent},
  {path: 'show_league', component: ShowLeagueDataComponent},
  {path: 'tipprunden-uebersicht', component: TipprundenuebersichtComponent},
  {path: 'meineTipprunden', component: MeinetipprundenComponent},


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
