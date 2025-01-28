import { CommonModule } from "@angular/common";
import { Component, OnInit } from "@angular/core";
import { FormsModule } from "@angular/forms";
import {
  HttpClient,
  HttpClientModule,
  HttpHeaders,
} from "@angular/common/http";
import { map, Observable, throwError } from "rxjs";
import { environment } from "../environments/environment";
import { catchError } from "rxjs/operators";

// Define an interface for the weather forecast data structure
interface WeatherForecast {
  dt: number;
  highTemp: number;
  lowTemp: number;
  description: string;
  windSpeed: number;
}

@Component({
  selector: "app-root",
  standalone: true,
  imports: [CommonModule, HttpClientModule, FormsModule],
  templateUrl: "./app.component.html",
  styleUrls: ["./app.component.css"],
})
export class AppComponent implements OnInit {
  constructor(private http: HttpClient) {}
  private apiUrl = environment.apiUrl; // URL for the API, defined in the environment file
  forecast: WeatherForecast[] = [];
  weatherLocation: string = "";
  cityName: string = "";
  title = "frontend"; // Title of the app
  weatherData = { location: { name: "" }, forecast: [] };


  ngOnInit() {}

  // Convert temperature from Kelvin to Celsius and format it to 2 decimal places
  kelvinToCelsius(tempK: number): string {
    return (tempK - 273.15).toFixed(2);
  }

  // Provide weather recommendations based on the day's weather forecast
  getRecommendation(day: WeatherForecast): string {
    if (parseFloat(this.kelvinToCelsius(day.highTemp)) > 40)
      return "Use sunscreen lotion";
    if (day.description.includes("rain")) return "Carry umbrella";
    if (day.windSpeed > 10) return "It’s too windy, watch out!";
    if (day.description.includes("Thunderstorm"))
      return "Don’t step out! A Storm is brewing!";
    return "";
  }

  // Fetch weather data for the specified city and update the component's state
  getData() {
    this.getWeather(this.cityName).subscribe({
      next: (data) => {
        this.weatherData = data;
        console.log("cityName", this.cityName);
        this.weatherLocation = this.cityName;
        console.log("weatherData", this.weatherData);
        this.forecast = this.weatherData.forecast;
      },
      error: (error) => {
        console.error("Error fetching weather data:", error.message);
        // Display an error message to the user
        alert("Failed to fetch weather data. Please try again later.");
      },
    });
  }

  // Make an HTTP POST request to get weather data for a specific city
  getWeather(cityName: string): Observable<any> {
    const headers = new HttpHeaders({
      "Content-Type": "application/json",
    });

    return this.http
      .post(this.apiUrl, '{"city": "' + cityName + '"}', { headers: headers })
      .pipe(
        catchError((error) => {
          console.error("Error occurred:", error);
          // Handle the error here, e.g., by showing an error message to the user
          return throwError(
            () => new Error("Something went wrong, please try again later.")
          );
        })
      );
  }
}
