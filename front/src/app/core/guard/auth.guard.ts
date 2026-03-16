import { inject } from '@angular/core';
import { Router, CanActivateFn } from '@angular/router';
import { TokenStorageService } from '../../stores/TokenStorageService';
import { jwtDecode } from 'jwt-decode';
import { TokenData } from '../../types/TokenData';

export function isAuthenticated(): CanActivateFn {
    return () => {
        const tokenService = inject(TokenStorageService);
        const router = inject(Router);

        const token = tokenService.getToken();

        if (token) {
            try {
                const data = jwtDecode<TokenData>(token);
                if (data.exp * 1000 > Date.now()) {
                    return true;
                }
                console.warn('Token expired');
            } catch {
                console.error('Invalid token');
            }
        }

        tokenService.removeToken();
        return router.createUrlTree(['/login']);
    };
}

export function requiredRole(...roles: string[]): CanActivateFn {
    return () => {
        const tokenService = inject(TokenStorageService);
        const router = inject(Router);

        const token = tokenService.getToken();

        if (token) {
            try {
                const decodedToken = jwtDecode<TokenData>(token);
                const userRoles = decodedToken.roles || [];

                if (userRoles.some(role => roles.includes(role))) {
                    return true;
                }
                console.warn('User does not have required role');
            } catch {
                console.error('Invalid token');
                tokenService.removeToken();
                return router.createUrlTree(['/login']);
            }
        }

        return router.createUrlTree(['/forbidden']);
    };
}
