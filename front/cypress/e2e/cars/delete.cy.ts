describe('Delete Car', () => {
    beforeEach(() => {
        cy.login(["ROLE_ADMIN"]);
        cy.mockCars();
    });

    it('should delete a car successfully', () => {
        cy.visit('/cars');
        cy.wait('@getCars');

        cy.get(':nth-child(1) > .cdk-column-actions > :nth-child(3)').click();
        cy.url().should('include', '/cars/1/delete');
        cy.deleteCar('1');
        cy.get('[type="submit"]').click();
        cy.get('[mat-raised-button=""]').click();
        cy.wait('@deleteCar');
        cy.get('app-custom-snack-bar > .mat-mdc-snack-bar-label').should('contain', 'Car deleted successfully');
    });
    it('should show error on deleting a car', () => {
        cy.visit('/cars');
        cy.wait('@getCars');

        cy.get(':nth-child(1) > .cdk-column-actions > :nth-child(3)').click();
        cy.url().should('include', '/cars/1/delete');
        cy.intercept('DELETE', `**/v1/cars/1`, (req) => {
            req.reply({
                statusCode: 500,
                body: "Car failed to delete"
            });
        }).as('deleteCarError');
        cy.get('[type="submit"]').click();
        cy.get('[mat-raised-button=""]').click();
        cy.wait('@deleteCarError');
        cy.get('app-custom-snack-bar > .mat-mdc-snack-bar-label').should('contain', 'Failed to delete car with id 1');
        cy.get('.error').should('contain', 'Failed to delete car.');
    });
});