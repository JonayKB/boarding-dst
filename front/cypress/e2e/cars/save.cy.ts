describe('Save Car', () => {
    beforeEach(() => {
        cy.login(["ROLE_ADMIN"]);
        cy.mockCars();
    });

    it('should show info on creating a car', () => {
        cy.visit('/cars/add');
        cy.saveCar();
        cy.get('form').within(() => {
            cy.get('input[formcontrolname="brand"]').type('Toyota');
            cy.get('input[formcontrolname="model"]').type('Corolla');
            cy.get('input[formcontrolname="year"]').type('2020');
            cy.get('input[formcontrolname="plate"]').type('ABC1234');
            cy.get('button[type="submit"]').click();
        });
        cy.wait('@saveCar');
        cy.get('app-custom-snack-bar > .mat-mdc-snack-bar-label').should('contain', 'Car saved successfully');
    });

    it('should show error on creating a car', () => {
        cy.visit('/cars/add');
        cy.intercept('POST', '**/v1/cars/', (req) => {
            req.reply({
                statusCode: 500,
                body: "Car failed to create"
            });
        }).as('saveCarError');
        cy.get('form').within(() => {
            cy.get('input[formcontrolname="brand"]').type('Toyota');
            cy.get('input[formcontrolname="model"]').type('Corolla');
            cy.get('input[formcontrolname="year"]').type('2020');
            cy.get('input[formcontrolname="plate"]').type('INVALID_PLATE').focus().blur();
            cy.get('button[type="submit"]').should('be.disabled');
        });
        cy.get('#mat-mdc-error-0').should('contain', 'Plate format must be ABC1234.');
        cy.get('input[formcontrolname="plate"]').clear().type('ABC1234');
        cy.get('button[type="submit"]').should('not.be.disabled').click();
        cy.wait('@saveCarError');
        cy.get('app-custom-snack-bar > .mat-mdc-snack-bar-label').should('contain', 'Failed to save car');
        cy.get('.error').should('contain', 'Failed to save car.');
    });
});