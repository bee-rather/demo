import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule, HttpHeaders } from '@angular/common/http';
import { map, Observable } from 'rxjs';

interface WeatherForecast {
  dt: number;
  highTemp: number;
  lowTemp:number
  description: string;
  windSpeed: number;
}

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, HttpClientModule, FormsModule],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  constructor(private http: HttpClient) {   }

  private apiUrl = 'http://localhost:8080/api/weather/forecast'; 
  forecast: WeatherForecast[] = [];
  weatherLocation: any = "";
  cityName = "";
  title = 'frontend';
  weatherData = {
    location: {
      name: ""
    },
    forecast: [
      
    ]
  };

  ngOnInit() {  }

  kelvinToCelsius(tempK: number): string {
    return (tempK - 273.15).toFixed(2);
  }

  getRecommendation(day: WeatherForecast): string {
    if (parseFloat(this.kelvinToCelsius(day.highTemp)) > 40) return 'Use sunscreen lotion';
    if (day.description.includes('rain')) return 'Carry umbrella';
    if (day.windSpeed > 10) return 'It’s too windy, watch out!';
    if (day.description.includes('Thunderstorm')) return 'Don’t step out! A Storm is brewing!';
    return '';
  }

  getData() {
    this.getWeather(this.cityName).subscribe(data => {
      this.weatherData = data;
      console.log("cityName", this.cityName);
    this.weatherLocation = this.cityName;
    console.log("weatherData", this.weatherData);
    this.forecast = this.weatherData.forecast;
    });
    
  }

  getWeather(cityName: string): Observable<any> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    return this.http.post(this.apiUrl, "{\"city\": \"" + cityName + "\"}", { headers: headers });
  }
}
