import { Component } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { WeatherService } from '../services/weather.service';

@Component({
  selector: 'app-weather',
  templateUrl: './weather.component.html',
  styleUrls: ['./weather.component.css']
})
export class WeatherComponent {
  city = '';
  weather: any;
  errorMessage = '';
  isLoading = false;

  constructor(private weatherService: WeatherService) {}

  getWeather() {
    const city = this.city.trim();

    if (!city) {
      this.weather = null;
      this.errorMessage = 'Please enter a city.';
      return;
    }

    this.weather = null;
    this.errorMessage = '';
    this.isLoading = true;

    this.weatherService.getWeather(this.city).subscribe(
      data => {
        this.weather = data;
        this.isLoading = false;
      },
      (error: HttpErrorResponse) => {
        console.error('Error fetching weather data', error);
        this.isLoading = false;
        this.errorMessage = this.getErrorMessage(error);
      }
    );
  }

  private getErrorMessage(error: HttpErrorResponse): string {
    if (error.status === 0) {
      return 'Weather server is unreachable.';
    }

    if (error.status === 404) {
      return 'City not found.';
    }

    if (error.status === 503) {
      return 'Weather data is currently unavailable.';
    }

    return 'Unable to fetch weather data.';
  }
}


