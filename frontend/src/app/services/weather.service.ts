import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Weather {
  city: string;
  country: string;
  latitude: number;
  longitude: number;
  temperature: number;
  humidity: number;
  windSpeed: number;
  weatherCode: number;
  description: string;
  time: string;
}

@Injectable({
  providedIn: 'root'
})
export class WeatherService {
  private readonly apiUrl = 'http://localhost:8080/api/weather';

  constructor(private http: HttpClient) { }

  getWeather(city: string): Observable<Weather> {
    const params = new HttpParams().set('city', city);

    return this.http.get<Weather>(this.apiUrl, { params });
  }
}
