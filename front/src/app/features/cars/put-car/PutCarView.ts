import { Component, signal } from "@angular/core";
import { FormBuilder, FormsModule, ReactiveFormsModule, Validators } from "@angular/forms";
import { CarsApiService } from "../../../api/CarsApiService";
import { CommonModule } from "@angular/common";
import { ActivatedRoute, RouterModule } from "@angular/router";

@Component({
    selector: 'app-put-car',
    templateUrl: './put-car-view.html',
    styleUrl: './put-car-view.css',
    imports: [FormsModule, ReactiveFormsModule, CommonModule, RouterModule]
})
export class PutCarView {
    errorMessage = signal('');
    lastSuccessMessage = signal('');
    currentYear = new Date().getFullYear();
    createCarForm;



    constructor(
        private fb: FormBuilder,
        private carsApiService: CarsApiService,
        private route: ActivatedRoute
    ) {
        const id = this.route.snapshot.paramMap.get('id')!;
        this.createCarForm = this.fb.group({
            carId: [{ value: '', disabled: true }],
            brand: ['', [Validators.required]],
            model: ['', [Validators.required]],
            year: [null as number | null, [Validators.required, Validators.min(1886), Validators.max(this.currentYear)]],
            plate: ['', [Validators.required, Validators.pattern(/^[A-Z]{3}\d{4}$/)]],
        });

        carsApiService.getCarById(id).subscribe({
            next: (car) => {
                console.log('Car fetched successfully for editing:', car);
                this.createCarForm.patchValue({
                    carId: car.id,
                    brand: car.brand,
                    model: car.model,
                    year: car.year,
                    plate: car.plate,
                });
            },
            error: (err) => {
                console.error('Error fetching car for editing:', err);
                this.errorMessage.set('Failed to fetch car for editing.');
            }
        });
    }

    updateCar() {
        if (this.createCarForm.valid) {
            const { brand, model, year, plate } = this.createCarForm.value;
            const saveCarData = {
                brand: brand!,
                model: model!,
                year: Number(year!),
                plate: plate!,
            };

            this.carsApiService.updateCar(
                this.route.snapshot.paramMap.get('id')!,
                saveCarData
            ).subscribe({
                next: (response) => {
                    console.log('Car updated successfully:', response);
                    this.lastSuccessMessage.set(response);
                    this.errorMessage.set('');
                },
                error: (err) => {
                    console.error('Error updating car:', err);
                    this.errorMessage.set('Failed to update car.');
                }
            });
        } else {
            this.createCarForm.markAllAsTouched();
            this.errorMessage.set('Please fill out all required fields correctly.');
        }
    }
}