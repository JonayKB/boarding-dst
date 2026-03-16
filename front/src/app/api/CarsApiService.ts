import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { catchError, Observable, } from 'rxjs';
import { PaginatedResponse } from '../types/PaginatedResponse';
import { SaveCar } from '../types/SaveCar';
import { Car } from '../types/Car';

@Injectable({
    providedIn: 'root'
})
export class CarsApiService {
    private apiUrl = environment.apiUrl;

    constructor(private http: HttpClient) { }

    getCars(page: number = 0, filterRequest?: any, sortRequest?: any): Observable<PaginatedResponse<Car>> {
        let params: any = { page };
        if (filterRequest) {
            Object.entries(filterRequest).forEach(([key, value]) => {
                if (value !== null && value !== undefined && value !== '') {
                    params[key] = value;
                }
            });
        }
        if (sortRequest?.sortBy) {
            params['sortBy'] = sortRequest.sortBy;
            params['asc'] = sortRequest.asc;
        }

        return this.http.get<PaginatedResponse<Car>>(`${this.apiUrl}/v1/cars/`, { params }).pipe(
            catchError((error) => {
                console.error('Failed to fetch cars:', error);
                throw error;
            })
        );
    }


    saveCars(saveCar: SaveCar): Observable<any> {
        return this.http.post(`${this.apiUrl}/v1/cars/`, saveCar, { responseType: 'text' }).pipe(
            catchError((error) => {
                console.error('Failed to save car:', error);
                throw error;
            })
        );
    }

    getCarById(id: string): Observable<Car> {
        return this.http.get<Car>(`${this.apiUrl}/v1/cars/${id}/`).pipe(
            catchError((error) => {
                console.error(`Failed to fetch car with id ${id}:`, error);
                throw error;
            })
        );
    }

    updateCar(id: string, saveCar: SaveCar): Observable<any> {
        return this.http.put(`${this.apiUrl}/v1/cars/${id}/`, saveCar, { responseType: 'text' }).pipe(
            catchError((error) => {
                console.error(`Failed to update car with id ${id}:`, error);
                throw error;
            })
        );
    }

    deleteCar(id: string): Observable<any> {
        return this.http.delete(`${this.apiUrl}/v1/cars/${id}/`, { responseType: 'text' }).pipe(
            catchError((error) => {
                console.error(`Failed to delete car with id ${id}:`, error);
                throw error;
            })
        );
    }
}