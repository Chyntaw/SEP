import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {HttpClientModule, HttpHeaders, HttpParams} from "@angular/common/http";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatIconModule} from '@angular/material/icon';
import {RouterModule} from '@angular/router';
import { TippworldheaderComponent } from './components/zyklus1/tippworldheader/tippworldheader.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { LoginComponent } from './components/zyklus1/login/login.component';
import { RegistrationComponent } from './components/zyklus1/registration/registration.component';
import { ZweiFaComponent } from './components/zyklus1/zwei-fa/zwei-fa.component';
import {RouterOutlet} from "@angular/router";
import { DashboardComponent } from './components/zyklus1/dashboard/dashboard.component';
import { AdminRegistrationComponent } from './components/zyklus1/admin-registration/admin-registration.component';
import { AdminDashboardComponent } from './components/zyklus1/admin-dashboard/admin-dashboard.component';
import { CreateLeagueComponent } from './components/zyklus1/create-league/create-league.component';
import { SystemdatumComponent } from './components/zyklus1/systemdatum/systemdatum.component';
import { ShowLeagueDataComponent } from './components/zyklus1/show-league-data/show-league-data.component';
import {MeinetipprundenComponent} from "./components/zyklus2/meinetipprunden/meinetipprunden.component";
import {TipprundenuebersichtComponent} from "./components/zyklus2/tipprundenuebersicht/tipprundenuebersicht.component";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatSelectModule} from "@angular/material/select";
import {Ng2SearchPipeModule} from "ng2-search-filter";
import {UserProfileComponent} from "./components/zyklus2/user-profile/user-profile.component";
import {FriendListComponent} from "./components/zyklus2/friend-list/friend-list.component";
import { ShowTopComponent } from './components/zyklus2/show-top/show-top.component';
import { MyProfileComponent } from './components/zyklus2/myProfile/my-profile/my-profile.component';
import {EinladungsUebersichtComponent} from "./components/zyklus2/einladungs-uebersicht/einladungs-uebersicht.component";
import {MatTableModule} from "@angular/material/table";
import { FreischaltungenComponent } from './components/zyklus3/freischaltungen/freischaltungen.component';
import { WettenComponent } from './components/zyklus3/wetten/wetten.component';
import { MinigameComponent } from './components/zyklus3/minigame/minigame.component';


@NgModule({
  declarations: [
    AppComponent,
    TippworldheaderComponent,
    LoginComponent,
    RegistrationComponent,
    DashboardComponent,
    ZweiFaComponent,
    AdminRegistrationComponent,
    AdminDashboardComponent,
    CreateLeagueComponent,
    SystemdatumComponent,
    ShowLeagueDataComponent,
    TipprundenuebersichtComponent,
    MeinetipprundenComponent,
    UserProfileComponent,
    FriendListComponent,
    MeinetipprundenComponent,
    MeinetipprundenComponent,
    ShowTopComponent,
    MyProfileComponent,
    EinladungsUebersichtComponent,
    FreischaltungenComponent,
    WettenComponent,
    MinigameComponent,


  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    MatIconModule,
    RouterOutlet,
    RouterModule,
    MatFormFieldModule,
    MatSelectModule,
    Ng2SearchPipeModule,
    MatTableModule

  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
