import { Component, signal } from "@angular/core";
import {  FormsModule} from "@angular/forms";
import { CarsApiService } from "../../../api/CarsApiService";
import { CommonModule } from "@angular/common";
import { Car } from "../../../types/Car";
import { ActivatedRoute, RouterModule } from "@angular/router";
import { TokenStorageService } from "../../../stores/TokenStorageService";
import { jwtDecode } from "jwt-decode";
import { TokenData } from "../../../types/TokenData";

@Component({
    selector: 'app-get-car',
    templateUrl: './get-car-view.html',
    styleUrl: './get-car-view.css',
    imports: [FormsModule, CommonModule, RouterModule]
})
export class GetCarView {
    errorMessage = signal('');
    lastSuccessCar = signal<Car | null>(null);

    constructor(private carsApiService: CarsApiService, private route: ActivatedRoute, private tokenStorageService: TokenStorageService) {
        console.log('GetCarView initialized with route params:', this.route.snapshot.paramMap.get('id'));
        this.getCarById(this.route.snapshot.paramMap.get('id')!);

    }

    getCarById(carId: string) {
        if (carId) {
            this.carsApiService.getCarById(carId).subscribe({
                next: (car) => {
                    console.log('Car fetched successfully:', car);
                    this.errorMessage.set('');
                    this.lastSuccessCar.set(car);
                },
                error: (err) => {
                    console.error('Error fetching car:', err);
                    this.errorMessage.set('Failed to fetch car. Please check the ID and try again.');
                    this.lastSuccessCar.set(null);
                }
            })
        }
    };

    hasRole(...roles: string[]): boolean {
        const token = this.tokenStorageService.getToken();
        if (!token) return false;
        try {
            const decoded = jwtDecode<TokenData>(token);
            return decoded.roles?.some(role => roles.includes(role)) ?? false;
        } catch {
            return false;
        }
    }

}