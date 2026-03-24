import { Component, signal } from "@angular/core";
import { FormBuilder, FormsModule, ReactiveFormsModule, Validators } from "@angular/forms";
import { CarsApiService } from "../../../api/CarsApiService";
import { CommonModule } from "@angular/common";
import { ActivatedRoute, RouterModule } from "@angular/router";
import { NotificationService } from "../../../shared/services/NotificationService";
import { Spinner } from "../../../shared/spinner/Spinner";

@Component({
    selector: 'app-put-car',
    templateUrl: './put-car-view.html',
    styleUrl: './put-car-view.css',
    imports: [FormsModule, ReactiveFormsModule, CommonModule, RouterModule, Spinner]
})
export class PutCarView {
    errorMessage = signal('');
    lastSuccessMessage = signal('');
    currentYear = new Date().getFullYear();
    createCarForm;
    loading = signal(false);



    constructor(
        private fb: FormBuilder,
        private carsApiService: CarsApiService,
        private route: ActivatedRoute,
        private notificationService: NotificationService
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
                this.notificationService.add({
                    title: "Info",
                    message: "Car fetched successfully for editing.",
                    type: "info",
                    duration: 5000,
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
            this.loading.set(true);
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
                    this.loading.set(false);
                    this.notificationService.add({
                        title: "Success",
                        message: "Car updated successfully",
                        type: "success",
                        duration: 5000,
                    });
                },
                error: (err) => {
                    console.error('Error updating car:', err);
                    this.errorMessage.set('Failed to update car.');
                    this.loading.set(false);
                }
            });
        } else {
            this.createCarForm.markAllAsTouched();
            this.errorMessage.set('Please fill out all required fields correctly.');
        }
    }
}