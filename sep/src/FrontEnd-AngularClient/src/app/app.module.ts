import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {HttpClientModule} from "@angular/common/http";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatIconModule} from '@angular/material/icon';
import {RouterModule} from '@angular/router';
import { TippworldheaderComponent } from './components/tippworldheader/tippworldheader.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { LoginComponent } from './components/login/login.component';
import { RegistrationComponent } from './components/registration/registration.component';
import { ZweiFAComponent } from './components/zwei-fa/zwei-fa.component';
import {RouterOutlet} from "@angular/router";



@NgModule({
  declarations: [
    AppComponent,
    TippworldheaderComponent,
    LoginComponent,
    RegistrationComponent,
    ZweiFAComponent
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
