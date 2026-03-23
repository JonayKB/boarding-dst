import { Injectable, signal } from '@angular/core';
import { Toast } from '../../types/Toast';

@Injectable({ providedIn: 'root' })
export class NotificationService {
  toasts = signal<Toast[]>([]);

  add(toast: Toast) {
    this.toasts.update(t => [...t, toast]);

    const duration = toast.duration ?? 3500;
    setTimeout(() => {
      this.toasts.update(t => t.filter(t2 => t2 !== toast));
    }, duration);
  }

  remove(index: number) {
    this.toasts.update(t => t.filter((_, i) => i !== index));
  }
}