import { Component, inject } from "@angular/core";
import { MatDialogActions, MatDialogClose, MatDialogContent, MatDialogTitle, MAT_DIALOG_DATA } from "@angular/material/dialog";

@Component({
    selector: 'app-delete-dialog',
    templateUrl: './detele-dialog.html',
    styleUrl: './delete-dialog.css',
    imports: [MatDialogActions, MatDialogContent, MatDialogTitle, MatDialogClose]
})
export class DeleteDialog {

    onConfirm = inject<{ onConfirm: () => void }>(MAT_DIALOG_DATA).onConfirm;

}