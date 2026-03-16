import { Component, signal } from "@angular/core";
import { CarsApiService } from "../api/CarsApiService";
import { FormsModule } from "@angular/forms";
import { CommonModule, NgIf } from "@angular/common";
import { PaginatedResponse } from "../types/PaginatedResponse";
import { Car } from "../types/Car";
import { AuthService } from "../api/AuthService";

@Component({
    selector: 'app-cars-view',
    templateUrl: './cars-view.html',
    styleUrl: './cars-view.css',
    imports: [FormsModule, CommonModule]
})
export class CarsView {
    email = signal('');

    password = signal('');
    token = signal('');
    errorMessage = signal('');
    carsPaginated = signal<PaginatedResponse<Car> | null>(null);
    constructor(private carsApiService: CarsApiService, private authService: AuthService) { }

    loadCars() {
        if (!this.token()) {
            this.errorMessage.set('Please enter a token to fetch cars.');
            return;
        }
        console.log('Fetching cars with token:', this.token());



        this.carsApiService.getCars(this.token()).subscribe({
            next: (data) => {
                this.carsPaginated.set(data);
                this.errorMessage.set('');
                console.log('Fetched cars:', data);
            },
            error: (err) => {
                console.error('Error fetching cars:', err);
                this.errorMessage.set('Failed to fetch cars.');
            }
        });
    }

    login() {
        this.authService.login(this.email(), this.password()).subscribe({
            next: (token) => {
                this.token.set(token);
                this.errorMessage.set('');
            },
            error: (err) => {
                console.error('Login failed:', err);
                this.errorMessage.set('Login failed. Please check your credentials.');
            }
        });
    }

    logout() {
        this.token.set('');
        this.carsPaginated.set(null);
        this.errorMessage.set('');
    }


}