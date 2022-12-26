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
import {UserProfileComponent} from "./components/zyklus2/user-profile/user-profile.component";
import {FriendListComponent} from "./components/zyklus2/friend-list/friend-list.component";

import {ShowTopComponent} from "./components/zyklus2/show-top/show-top.component";
import {MyProfileComponent} from "./components/zyklus2/myProfile/my-profile/my-profile.component";
import {
  EinladungsUebersichtComponent
} from "./components/zyklus2/einladungs-uebersicht/einladungs-uebersicht.component";
import {FreischaltungenComponent} from "./components/zyklus3/freischaltungen/freischaltungen.component";

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
  {path: 'getUser', component: UserProfileComponent},
  {path: 'friend-list', component: FriendListComponent},
  {path: 'showTop', component: ShowTopComponent},
  {path: 'my-Profile', component: MyProfileComponent},
  {path: 'einladungs-uebersicht/:id', component: EinladungsUebersichtComponent},
  {path: 'freischaltungen', component: FreischaltungenComponent},



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
