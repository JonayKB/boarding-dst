import { Component } from "@angular/core";

@Component({
    selector: 'app-forbidden',
    template: `
        <h1>403 - Forbidden</h1>
        <p>You do not have permission to access this page.</p>
    `,
    styles: `
        h1 {
            color: red;
        }
        p {
            font-size: 18px;
        }
    `
})
export class ForbiddenView {

}