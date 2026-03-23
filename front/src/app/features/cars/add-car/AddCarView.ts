import { Component, signal } from "@angular/core";
import { FormBuilder, FormsModule, ReactiveFormsModule, Validators } from "@angular/forms";
import { CarsApiService } from "../../../api/CarsApiService";
import { CommonModule } from "@angular/common";
import { NotificationService } from "../../../shared/services/NotificationService";

@Component({
    selector: 'app-add-car',
    templateUrl: './add-car-view.html',
    styleUrl: './add-car-view.css',
    imports: [FormsModule, ReactiveFormsModule, CommonModule]
})
export class AddCarView {
    createCarForm;
    errorMessage = signal('');
    lastSuccessMessage = signal('');
    currentYear = new Date().getFullYear();

    constructor(private fb: FormBuilder, private carsApiService: CarsApiService, private notificationService: NotificationService) {
        this.createCarForm = this.fb.group({
            brand: ['', [Validators.required]],
            model: ['', [Validators.required]],
            year: [
                '',
                [Validators.required, Validators.min(1886), Validators.max(this.currentYear)],
            ],
            plate: ['', [Validators.required, Validators.pattern(/^[A-Z]{3}\d{4}$/)]],
        });
    }

    createCar() {
        if (this.createCarForm.valid) {
            const { brand, model, year, plate } = this.createCarForm.value;
            const saveCarData = {
                brand: brand!,
                model: model!,
                year: Number(year!),
                plate: plate!,
            };

            this.carsApiService.saveCars(saveCarData).subscribe({
                next: (response) => {
                    console.log('Car saved successfully:', response);
                    this.createCarForm.reset();
                    this.lastSuccessMessage.set(response);
                    this.notificationService.add({
                        title: "Success",
                        message: "Car saved successfully",
                        type: "success",
                        duration: 5000,
                    });
                },
                error: (err) => {
                    console.error('Error saving car:', err);
                    this.errorMessage.set('Failed to save car.');
                }
            });
        } else {
            this.createCarForm.markAllAsTouched();
            this.errorMessage.set('Please fill out all required fields correctly.');
        }
    }
}