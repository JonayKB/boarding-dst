import { Component } from "@angular/core";
import { NotificationService } from "../services/NotificationService";
import { Toast } from "../../types/Toast";

@Component({
    selector: "app-toast-container",
    templateUrl: "./toast-container.html",
    styleUrls: ["./toast-container.css"],
})
export class ToastContainerComponent {
    constructor(protected notificationService: NotificationService) {
    }
    removeToast(index: number) {
        this.notificationService.remove(index);
    }

    getDuration(toast: Toast): string {
        return `${toast.duration ?? 3500}ms`;
    }
}