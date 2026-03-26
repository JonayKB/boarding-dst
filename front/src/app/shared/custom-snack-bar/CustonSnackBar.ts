import { Component, inject, Inject } from "@angular/core";
import { MAT_SNACK_BAR_DATA, MatSnackBarAction, MatSnackBarActions, MatSnackBarLabel, MatSnackBarRef } from "@angular/material/snack-bar";
import { Snack } from "../../types/Snack";
import { CommonModule } from "@angular/common";

@Component({
    selector: 'app-custom-snack-bar',
    templateUrl: './custom-snack-bar.html',
    styleUrl: './custom-snack-bar.css',
    imports: [MatSnackBarAction, CommonModule, MatSnackBarActions, MatSnackBarLabel]
})
export class CustomSnackBarComponent {

    snackBarRef = inject(MatSnackBarRef);
    snack = inject<Snack>(MAT_SNACK_BAR_DATA);

}