describe('Cars', () => {
    beforeEach(() => {
        cy.login();
        cy.mockCars();
    });

    it('should show cars list', () => {
        cy.visit('/cars');
        cy.wait('@getCars');
        cy.get('h1').should('contain', 'Cars');
        cy.get('table').should('exist');
        cy.get('table tbody tr').should('have.length', 2);
    });

    it('should show pagination controls when there are more than 10 cars', () => {
        cy.mockCarsPagination(25, 5);
        cy.visit('/cars');
        cy.wait('@getCars');
        cy.get('.mat-mdc-paginator').should('exist');
        cy.get('table tbody tr').should('have.length', 5);

        cy.get('.mat-mdc-paginator .mat-mdc-paginator-page-size').should('contain', '5');
        cy.get('.mat-mdc-paginator .mat-mdc-paginator-range-label').should('contain', '1 – 5 of 25');

        cy.get('.mat-mdc-paginator .mat-mdc-paginator-navigation-next').click();
        cy.wait('@getCars');
        cy.get('table tbody tr').should('have.length', 5);
        cy.get('.mat-mdc-paginator .mat-mdc-paginator-range-label').should('contain', '6 – 10 of 25');

        cy.get('.mat-mdc-paginator .mat-mdc-paginator-navigation-next').click();
        cy.wait('@getCars');
        cy.get('table tbody tr').should('have.length', 5);
        cy.get('.mat-mdc-paginator .mat-mdc-paginator-range-label').should('contain', '11 – 15 of 25');

        cy.get('.mat-mdc-paginator .mat-mdc-paginator-navigation-next').click();
        cy.wait('@getCars');
        cy.get('table tbody tr').should('have.length', 5);
        cy.get('.mat-mdc-paginator .mat-mdc-paginator-range-label').should('contain', '16 – 20 of 25');

        cy.get('.mat-mdc-paginator .mat-mdc-paginator-navigation-next').click();
        cy.wait('@getCars');
        cy.get('table tbody tr').should('have.length', 5);
        cy.get('.mat-mdc-paginator .mat-mdc-paginator-range-label').should('contain', '21 – 25 of 25');

        cy.get('.mat-mdc-paginator .mat-mdc-paginator-navigation-previous').click();
        cy.wait('@getCars');
        cy.get('table tbody tr').should('have.length', 5);
        cy.get('.mat-mdc-paginator .mat-mdc-paginator-range-label').should('contain', '16 – 20 of 25');
    });

    it('should show car details when clicking on a car', () => {
        cy.visit('/cars');
        cy.wait('@getCars');
        cy.mockCar('1', 'Brand 1', 'Model 1');
        cy.get(':nth-child(1) > .cdk-column-actions > :nth-child(1)').click();
        cy.wait('@getCar');
        cy.url().should('include', '/cars/1');
        cy.get('h1').should('contain', 'Get Car by ID');
        cy.get('app-get-car > :nth-child(1) > div > :nth-child(2)').should('contain', 'ID:', '1');
        cy.get('app-get-car > :nth-child(1) > div > :nth-child(3)').should('contain', 'Brand:', 'Brand 1');
        cy.get('app-get-car > :nth-child(1) > div > :nth-child(4)').should('contain', 'Model:', 'Model 1');
        cy.get('app-get-car > :nth-child(1) > div > :nth-child(5)').should('contain', 'Year:', '2020');
        cy.get('app-get-car > :nth-child(1) > div > :nth-child(6)').should('contain', 'Plate:', 'ABC1234');
    });
});