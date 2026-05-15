import { Component } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { Weather, WeatherService } from '../services/weather.service';

@Component({
  selector: 'app-weather',
  templateUrl: './weather.component.html',
  styleUrls: ['./weather.component.css']
})
export class WeatherComponent {
  city = '';
  weather: Weather | null = null;
  errorMessage = '';
  isLoading = false;

  constructor(private weatherService: WeatherService) {}

  getWeather() {
    const city = this.city.trim();

    if (!city) {
      this.weather = null;
      this.errorMessage = 'Entre une ville.';
      return;
    }

    this.weather = null;
    this.errorMessage = '';
    this.isLoading = true;

    this.weatherService.getWeather(city).subscribe(
      data => {
        this.weather = data;
        this.isLoading = false;
      },
      (error: HttpErrorResponse) => {
        console.error('Erreur lors de la récupération de la météo', error);
        this.isLoading = false;
        this.errorMessage = this.getErrorMessage(error);
      }
    );
  }

  private getErrorMessage(error: HttpErrorResponse): string {
    if (error.status === 0) {
      return 'Le serveur météo est inaccessible.';
    }

    if (error.status === 404) {
      return 'Ville introuvable.';
    }

    if (error.status === 503) {
      return 'Les données météo sont momentanément indisponibles.';
    }

    return 'Impossible de récupérer la météo.';
  }

  getWeatherIcon(description: string): string {
    const weather = description.toLowerCase();

    if (weather.includes('dégagé')) {
      return '☀️';
    }

    if (weather.includes('pluie')) {
      return '🌧️';
    }

    if (weather.includes('neige')) {
      return '❄️';
    }

    if (weather.includes('orage')) {
      return '⛈️';
    }

    if (weather.includes('brouillard')) {
      return '🌫️';
    }

    return '☁️';
  }
}
