import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { catchError, Observable, } from 'rxjs';
import { PaginatedResponse } from '../types/PaginatedResponse';
import { Car } from '../types/Car';

@Injectable({
    providedIn: 'root'
})
export class CarsApiService {
    private apiUrl = environment.apiUrl;

    constructor(private http: HttpClient) { }

    getCars(token: string): Observable<PaginatedResponse<Car>> {
        return this.http.get<PaginatedResponse<Car>>(`${this.apiUrl}/v1/cars/`, {
            headers: { 'Authorization': `Bearer ${token}` },
            params: { page: 0 }
        }).pipe(
            catchError((error) => {
                console.error('Failed to fetch cars:', error);
                throw error;
            })
        );
    }
}