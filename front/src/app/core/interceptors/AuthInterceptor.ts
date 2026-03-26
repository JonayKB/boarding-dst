import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { catchError, Observable } from 'rxjs';
import { TokenStorageService } from '../../stores/TokenStorageService';
import { Router } from '@angular/router';
import { NotificationService } from '../../shared/services/NotificationService';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
    constructor(private tokenService: TokenStorageService, private router: Router, private notificationService: NotificationService) { }
    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        const token = this.tokenService.getToken();
        if (token) {
            const cloned = this.addTokenHeader(req, token);
            return next.handle(cloned).pipe(
                catchError((error) => {
                    switch (error.status) {
                        case 401:
                            return this.handle401Error(cloned, next);
                        case 403:
                            return this.handle403Error();
                        case 404:
                            return this.handle404Error();
                        case 409:
                            return this.handle409Error();
                        default:
                            console.error('HTTP error:', error);
                            this.notificationService.add({
                                title: "An unexpected error occurred.",
                                type: "error",
                                duration: 10000,
                            });
                    }
                    throw error;
                })
            );
        }
        return next.handle(req);
    }
    addTokenHeader(request: HttpRequest<any>, token: string): HttpRequest<any> {
        return request.clone({
            headers: request.headers.set('Authorization', `Bearer ${token}`)
        });

    }
    handle401Error(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        this.tokenService.removeToken();
        this.notificationService.add({
            title: "Session expired. Please log in again.",
            type: "error",
            duration: 10000,
        });
        this.router.navigate(['/login']);
        return next.handle(request);
    }
    handle403Error(): Observable<HttpEvent<any>> {
        this.notificationService.add({
            title: "You do not have permission to perform this action.",
            type: "error",
            duration: 10000,
        });
        this.router.navigate(['/forbidden']);
        return new Observable<HttpEvent<any>>();
    }
    handle404Error(): Observable<HttpEvent<any>> {
        this.notificationService.add({
            title: "The requested resource was not found.",
            type: "error",
            duration: 10000,
        });
        this.router.navigate(['/not-found']);
        return new Observable<HttpEvent<any>>();
    }
    handle409Error(): Observable<HttpEvent<any>> {
        this.notificationService.add({
            title: "Conflict occurred. Please check your request.",
            type: "error",
            duration: 10000,
        });
        this.router.navigate(['/conflict']);
        return new Observable<HttpEvent<any>>();
    }
}



