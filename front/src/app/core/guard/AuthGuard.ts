import { Injectable } from '@angular/core';
import { CanActivate, Router, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { TokenStorageService } from '../../stores/TokenStorageService';

@Injectable({
    providedIn: 'root'
})
export class SecuredRoute implements CanActivate {

    constructor(
        private tokenService: TokenStorageService,
        private router: Router
    ) { }

    canActivate(
        route: ActivatedRouteSnapshot,
        state: RouterStateSnapshot
    ): boolean {
        const token = this.tokenService.getToken();

        if (token) {
            return true;
        }

        this.router.navigate(['/login'], {
            queryParams: { returnUrl: state.url }
        });
        return false;
    }
}