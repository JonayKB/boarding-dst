import { Component, signal } from "@angular/core";
import { AuthService } from "../../../api/AuthService";
import { TokenStorageService } from "../../../stores/TokenStorageService";
import { Router } from '@angular/router';
import { CommonModule } from "@angular/common";
import { FormsModule } from "@angular/forms";
import { AuthStateService } from "../../../shared/services/AuthStateService";
@Component({
    selector: 'app-login',
    templateUrl: './login-view.html',
    styleUrl: './login-view.css',
    imports: [FormsModule, CommonModule,]
})
export class LoginView {
    email = '';
    password = '';
    errorMessage = signal('');

    constructor(private authState: AuthStateService, private authService: AuthService, private router: Router) { }

    login() {
        this.authService.login(this.email, this.password).subscribe({
            next: (token) => {
                console.log('Login successful, received token:', token);
                this.errorMessage.set('');
                this.authState.setToken(token);
                this.router.navigate(['/cars']);
            },
            error: (err) => {
                console.error('Login failed:', err);
                this.errorMessage.set('Login failed. Please check your credentials and try again.');
            }
        });
    }
}