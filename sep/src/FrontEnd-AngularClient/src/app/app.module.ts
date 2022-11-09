import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {HttpClientModule, HttpHeaders, HttpParams} from "@angular/common/http";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatIconModule} from '@angular/material/icon';
import {RouterModule} from '@angular/router';
import { TippworldheaderComponent } from './components/tippworldheader/tippworldheader.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { LoginComponent } from './components/login/login.component';
import { RegistrationComponent } from './components/registration/registration.component';
import { ZweiFaComponent } from './components/zwei-fa/zwei-fa.component';
import {RouterOutlet} from "@angular/router";
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { AdminRegistrationComponent } from './components/admin-registration/admin-registration.component';
import { AdminDashboardComponent } from './components/admin-dashboard/admin-dashboard.component';
import { CreateLeagueComponent } from './components/create-league/create-league.component';
import { SystemdatumComponent } from './components/systemdatum/systemdatum.component';
import { ShowLeagueDataComponent } from './components/show-league-data/show-league-data.component';





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
      RouterModule
    ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
