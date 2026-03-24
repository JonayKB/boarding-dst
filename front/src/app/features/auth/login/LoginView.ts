import { Component, signal } from "@angular/core";
import { AuthService } from "../../../api/AuthService";
import { Router } from '@angular/router';
import { CommonModule } from "@angular/common";
import { FormsModule } from "@angular/forms";
import { AuthStateService } from "../../../shared/services/AuthStateService";
import { NotificationService } from "../../../shared/services/NotificationService";
import { Spinner } from "../../../shared/spinner/Spinner";

@Component({
    selector: 'app-login',
    templateUrl: './login-view.html',
    styleUrl: './login-view.css',
    imports: [FormsModule, CommonModule, Spinner]
})
export class LoginView {
    email = '';
    password = '';
    errorMessage = signal('');
    loading = signal(false);

    constructor(private authState: AuthStateService, private authService: AuthService, private router: Router, private notificationService: NotificationService) { }

    login() {
        this.loading.set(true);
        this.authService.login(this.email, this.password).subscribe({
            next: (token) => {
                this.errorMessage.set('');
                this.authState.setToken(token);
                this.loading.set(false);
                this.router.navigate(['/cars']);
                this.notificationService.add({
                    title: "Success",
                    message: "Logged in successfully",
                    type: "success",
                    duration: 5000,
                });
            },
            error: (err) => {
                this.errorMessage.set('Invalid email or password');
                this.loading.set(false);
            }
        });
    }
}