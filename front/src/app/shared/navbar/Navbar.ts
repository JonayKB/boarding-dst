import { Component, inject } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { AuthStateService } from '../services/AuthStateService';

@Component({
    selector: 'app-navbar',
    templateUrl: './navbar.html',
    styleUrl: './navbar.css',
    imports: [RouterModule]
})
export class Navbar {
    menuOpen = false;

    constructor(protected auth: AuthStateService, protected router: Router) { }
    toggleMenu() { this.menuOpen = !this.menuOpen; }
    closeMenu() { this.menuOpen = false; }
    logout() {
        this.auth.clearToken();
        this.menuOpen = false;
        this.router.navigate(['/login']);
    }

    login() { this.router.navigate(['/login']); }
    signup() { this.router.navigate(['/signup']); }
}