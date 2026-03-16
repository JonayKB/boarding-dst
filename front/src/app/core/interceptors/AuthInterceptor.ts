import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { catchError, Observable } from 'rxjs';
import { TokenStorageService } from '../../stores/TokenStorageService';
import { Router } from '@angular/router';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
    constructor(private tokenService: TokenStorageService, private router: Router) { }
    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        const token = this.tokenService.getToken();
        if (token) {
            const cloned = this.addTokenHeader(req, token);
            return next.handle(cloned).pipe(
                catchError((error) => {
                    if (error.status === 401) {
                        return this.handle401Error(req, next);
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
        this.router.navigate(['/login']);
        return next.handle(request);
    }
}



