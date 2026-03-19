import { Component, signal } from "@angular/core";
import { CarsApiService } from "../../../api/CarsApiService";
import { CommonModule } from "@angular/common";
import { FormBuilder, ReactiveFormsModule } from "@angular/forms";
import { PaginatedResponse } from "../../../types/PaginatedResponse";
import { Car } from "../../../types/Car";
import { TokenStorageService } from "../../../stores/TokenStorageService";
import { RouterModule } from "@angular/router";
import { SortRequest } from "../../../types/SortRequest";
import { FilterRequest } from "../../../types/FilterRequest";

@Component({
    selector: 'app-cars-view',
    templateUrl: './cars-view.html',
    styleUrl: './cars-view.css',
    imports: [CommonModule, ReactiveFormsModule, RouterModule]
})
export class CarsView {
    errorMessage = signal('');
    carsPaginated = signal<PaginatedResponse<Car> | null>(null);
    currentPage = signal(0);
    filterForm;
    filtersOpen = true;

    sortOptions = [
        { label: 'Brand', value: 'brand' },
        { label: 'Model', value: 'model' },
        { label: 'Year', value: 'year' },
        { label: 'Plate', value: 'plate' },
    ];
    currentYear = new Date().getFullYear();

    constructor(
        private fb: FormBuilder,
        private carsApiService: CarsApiService,
        private tokenStorageService: TokenStorageService
    ) {
        this.filterForm = this.fb.group({
            brand: [''],
            model: [''],
            plate: [''],
            yearFrom: [null as number | null],
            yearTo: [null as number | null],
            sortBy: [''],
            asc: [true],
        });
        this.loadCars();
    }
    setSortBy(value: string) {
        this.filterForm.patchValue({ sortBy: value });
    }

    toggleAsc() {
        const current = this.filterForm.get('asc')?.value;
        this.filterForm.patchValue({ asc: !current });
    }

    loadCars(page: number = 0) {
        if (!this.tokenStorageService.getToken()) {
            this.errorMessage.set('Please enter a token to fetch cars.');
            return;
        }

        const { brand, model, plate, yearFrom, yearTo, sortBy, asc } = this.filterForm.value;

        const filterRequest = {
            brand: brand || undefined,
            model: model || undefined,
            plate: plate || undefined,
            yearFrom: yearFrom || undefined,
            yearTo: yearTo || undefined,
        } as FilterRequest;

        const sortRequest = {
            sortBy: sortBy || undefined,
            asc: asc ?? true,
        } as SortRequest;

        this.carsApiService.getCars(page, filterRequest, sortRequest).subscribe({
            next: (data) => {
                this.carsPaginated.set(data);
                this.currentPage.set(page);
                this.errorMessage.set('');
            },
            error: (err) => {
                console.error('Error fetching cars:', err);
                this.errorMessage.set('Failed to fetch cars.');
            }
        });
    }

    nextPage() {
        const total = this.carsPaginated()?.totalPages ?? 0;
        const next = this.currentPage() + 1;
        if (next < total) this.loadCars(next);
    }

    prevPage() {
        const prev = this.currentPage() - 1;
        if (prev >= 0) this.loadCars(prev);
    }

    resetFilters() {
        this.filterForm.reset({ asc: true });
        this.loadCars(0);
    }
}