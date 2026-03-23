import { Component, signal } from "@angular/core";
import { AuthService } from "../../../api/AuthService";
import { TokenStorageService } from "../../../stores/TokenStorageService";
import { Router } from '@angular/router';
import { CommonModule } from "@angular/common";
import { FormsModule } from "@angular/forms";
import { AuthStateService } from "../../../shared/services/AuthStateService";
import { NotificationService } from "../../../shared/services/NotificationService";
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

    constructor(private authState: AuthStateService, private authService: AuthService, private router: Router, private notificationService: NotificationService) { }

    login() {
        this.authService.login(this.email, this.password).subscribe({
            next: (token) => {
                this.errorMessage.set('');
                this.authState.setToken(token);
                this.router.navigate(['/cars']);
                this.notificationService.add({
                    title: "Success",
                    message: "Logged in successfully",
                    type: "success",
                    duration: 5000,
                });
            },
        });
    }
}