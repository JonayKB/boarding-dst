import { Component, signal } from "@angular/core";
import { FormBuilder, FormsModule, ReactiveFormsModule, Validators } from "@angular/forms";
import { CarsApiService } from "../../../api/CarsApiService";
import { CommonModule } from "@angular/common";
import { ActivatedRoute, Router } from "@angular/router";
import { NotificationService } from "../../../shared/services/NotificationService";
import { Spinner } from "../../../shared/spinner/Spinner";

@Component({
    selector: 'app-delete-car',
    templateUrl: './delete-car-view.html',
    styleUrl: './delete-car-view.css',
    imports: [FormsModule, ReactiveFormsModule, CommonModule, Spinner]
})
export class DeleteCarView {
    errorMessage = signal('');
    lastSuccessMessage = signal('');
    currentYear = new Date().getFullYear();
    showCarForm;
    loading = signal(false);



    constructor(
        private fb: FormBuilder,
        private carsApiService: CarsApiService,
        private route: ActivatedRoute,
        private router: Router,
        private notificationService: NotificationService
    ) {
        const id = this.route.snapshot.paramMap.get('id')!;
        this.showCarForm = this.fb.group({
            carId: [{ value: '', disabled: true }],
            brand: [{ value: '', disabled: true }, [Validators.required]],
            model: [{ value: '', disabled: true }, [Validators.required]],
            year: [{ value: 1886, disabled: true }, [Validators.required, Validators.min(1886), Validators.max(this.currentYear)]],
            plate: [{ value: '', disabled: true }, [Validators.required, Validators.pattern(/^[A-Z]{3}\d{4}$/)]],
        });

        carsApiService.getCarById(id).subscribe({
            next: (car) => {
                console.log('Car fetched successfully for deleting:', car);
                this.showCarForm.patchValue({
                    carId: car.id,
                    brand: car.brand,
                    model: car.model,
                    year: car.year,
                    plate: car.plate,
                });
                this.notificationService.add({
                    title: "Info",
                    message: "Car fetched successfully for deleting.",
                    type: "info",
                    duration: 5000,
                });
            },
            error: (err) => {
                console.error('Error fetching car for deleting:', err);
                this.errorMessage.set('Failed to fetch car for deleting.');
            }
        });
    }

    deleteCar() {
        this.loading.set(true);
        const carId = this.route.snapshot.paramMap.get('id');
        if (carId) {
            this.carsApiService.deleteCar(carId).subscribe({
                next: (response) => {
                    console.log('Car deleted successfully:', response);
                    this.lastSuccessMessage.set(response);
                    this.errorMessage.set('');
                    this.router.navigate(['/cars']);
                    this.loading.set(false);
                    this.notificationService.add({
                        title: "Success",
                        message: "Car deleted successfully",
                        type: "success",
                        duration: 5000,
                    });
                },
                error: (err) => {
                    console.error('Error deleting car:', err);
                    this.errorMessage.set('Failed to delete car.');
                    this.loading.set(false);
                }
            });
        }
    }

    cancelDelete() {
        this.router.navigate(['/cars/' + this.route.snapshot.paramMap.get('id')]);
    }
}