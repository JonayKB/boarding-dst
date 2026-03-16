import { Component, signal } from "@angular/core";
import { CarsApiService } from "../api/CarsApiService";
import { FormsModule } from "@angular/forms";
import { CommonModule, NgIf } from "@angular/common";
import { PaginatedResponse } from "../types/PaginatedResponse";
import { Car } from "../types/Car";
import { AuthService } from "../api/AuthService";
import { TokenStorageService } from "../stores/TokenStorageService";

@Component({
    selector: 'app-cars-view',
    templateUrl: './cars-view.html',
    styleUrl: './cars-view.css',
    imports: [CommonModule]
})
export class CarsView {

    errorMessage = signal('');
    carsPaginated = signal<PaginatedResponse<Car> | null>(null);
    constructor(private carsApiService: CarsApiService, private tokenStorageService: TokenStorageService) { }

    loadCars() {
        if (!this.tokenStorageService.getToken()) {
            this.errorMessage.set('Please enter a token to fetch cars.');
            return;
        }
        console.log('Fetching cars with token:', this.tokenStorageService.getToken());


        this.carsApiService.getCars().subscribe({
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


}