describe('Edit Car', () => {
    beforeEach(() => {
        cy.login(["ROLE_ADMIN"]);
        cy.mockCars();
    });

    it('should show info on editing a car', () => {
        cy.visit('/cars');
        cy.wait('@getCars');
        cy.mockCar('1', 'Brand 1', 'Model 1');
        cy.get(':nth-child(1) > .cdk-column-actions > :nth-child(2)').click();
        cy.wait('@getCar');
        cy.url().should('include', '/cars/1/edit');
        cy.get('#id').should('have.value', '1');
        cy.get('#brand').should('have.value', 'Brand 1');
        cy.get('#model').should('have.value', 'Model 1');
        cy.get('#year').should('have.value', '2020');
        cy.get('#plate').should('have.value', 'ABC1');
    });

    it('should show error on clicking plate input in edit form', () => {
        cy.visit('/cars');
        cy.wait('@getCars');
        cy.mockCar('1', 'Brand 1', 'Model 1');
        cy.get(':nth-child(1) > .cdk-column-actions > :nth-child(2)').click();
        cy.wait('@getCar');
        cy.url().should('include', '/cars/1/edit');
        cy.get('#plate').focus().blur();
        cy.get('.error').should('contain', 'Plate format must be ABC1234.');
    });

    it('should edit a car error', () => {
        cy.visit('/cars');
        cy.wait('@getCars');
        cy.mockCar('1', 'Brand 1', 'Model 1');
        cy.get(':nth-child(1) > .cdk-column-actions > :nth-child(2)').click();
        cy.wait('@getCar');
        cy.url().should('include', '/cars/1/edit');

        cy.get('#brand').clear().type('Updated Brand');
        cy.get('#model').clear().type('Updated Model');
        cy.get('#year').clear().type('2021');
        cy.get('#plate').clear().type('XYZ7890');
        cy.get('form').submit();

        cy.get('app-custom-snack-bar > .mat-mdc-snack-bar-label').should('contain', 'Failed to update car with id 1');
        cy.get('.error').should('contain', 'Failed to update car');

    });
    it('should edit a car successfully', () => {
        cy.visit('/cars');
        cy.wait('@getCars');
        cy.mockCar('1', 'Brand 1', 'Model 1');
        cy.get(':nth-child(1) > .cdk-column-actions > :nth-child(2)').click();
        cy.wait('@getCar');
        cy.editCar('1');
        cy.url().should('include', '/cars/1/edit');

        cy.get('#brand').clear().type('Updated Brand');
        cy.get('#model').clear().type('Updated Model');
        cy.get('#year').clear().type('2021');
        cy.get('#plate').clear().type('XYZ7890');
        cy.get('form').submit();

        cy.wait('@editCar');
        cy.get('p').should('contain', 'Car updated successfully');

        cy.get('app-custom-snack-bar > .mat-mdc-snack-bar-label').should('contain', 'Car updated successfully');
    });

    it('should back to car details on canceling edit', () => {
        cy.visit('/cars');
        cy.wait('@getCars');
        cy.mockCar('1', 'Brand 1', 'Model 1');
        cy.get(':nth-child(1) > .cdk-column-actions > :nth-child(2)').click();
        cy.wait('@getCar');
        cy.url().should('include', '/cars/1/edit');

        cy.get('form.ng-untouched > a').click();
        cy.url().should('include', '/cars/1');
    });
});