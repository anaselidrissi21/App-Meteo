import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { AppComponent } from './app.component';
import { WeatherComponent } from './weather/weather.component';
import { WeatherService } from './weather/weather.service';

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
