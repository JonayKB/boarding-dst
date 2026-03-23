import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { catchError, Observable, } from 'rxjs';
import { NotificationService } from '../shared/services/NotificationService';

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private apiUrl = environment.apiUrl;

    constructor(private http: HttpClient, private notificationService: NotificationService) { }

    login(email: string, password: string): Observable<string> {
        return this.http.post(`${this.apiUrl}/auth/login/`, { email, password }, { responseType: 'text' }).pipe(
            catchError((error) => {
                console.error('Failed to login:', error);
                this.notificationService.add({
                    title: "Error",
                    message: "Failed to login",
                    type: "error",
                    duration: 10000,
                });
                throw error;
            })
            
        );
    }
}