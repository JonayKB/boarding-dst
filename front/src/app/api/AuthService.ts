import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { catchError, Observable, } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private apiUrl = environment.apiUrl;

    constructor(private http: HttpClient) { }

    login(email: string, password: string): Observable<any> {
        return this.http.post(`${this.apiUrl}/auth/login/`, { email, password }, { responseType: 'text' }).pipe(
            catchError((error) => {
                console.error('Failed to login:', error);
                throw error;
            })
        );
    }
}