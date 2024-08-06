import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { AppComponent } from './app.component';
import { WeatherService } from './services/weather.service';
import { WeatherComponent } from './weather/weather.component';

@NgModule({
declarations: [
AppComponent,
WeatherComponent
],
imports: [
BrowserModule,
FormsModule,
HttpClientModule
],
providers: [WeatherService],
bootstrap: [AppComponent]
})
export class AppModule { }

