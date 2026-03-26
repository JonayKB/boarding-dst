import { Injectable, signal } from '@angular/core';
import { Snack } from '../../types/Snack';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CustomSnackBarComponent } from '../custom-snack-bar/CustonSnackBar';

@Injectable({ providedIn: 'root' })
export class NotificationService {
  snacks = signal<Snack[]>([]);
  constructor(private snackBar: MatSnackBar) { }

  add(snack: Snack) {
    if (!snack.action) {
      snack.action = 'Close';
    }
    this.snacks.update((current) => [...current, snack]);
    this.snackBar.openFromComponent(CustomSnackBarComponent, {
      data: snack,
      duration: snack.duration ?? 3500,
      panelClass: `snack-${snack.type || 'info'}`,
    });
  }

  remove(index: number) {
    this.snacks.update((current) => current.filter((_, i) => i !== index));
    this.snackBar.dismiss();
  }
}