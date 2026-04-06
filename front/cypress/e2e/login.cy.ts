
describe('Login', () => {
    it('should show error notification on failed login', () => {
        cy.visit('/login');
        cy.get('input[name="email"]').type('invalid-email');
        cy.get('input[name="password"]').type('wrong-password');
        cy.get('form').submit();
        cy.get('app-custom-snack-bar > .mat-mdc-snack-bar-label').should('contain', 'Failed to login');
        cy.get('.error').should('contain', 'Invalid email or password');
    });
    it('should login successfully and redirect to /cars', () => {
        const validToken = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwicm9sZXMiOlsiUk9MRV9BRE1JTiJdLCJleHAiOjQwNzA5MDg4MDB9.fake-signature';

        cy.intercept('POST', '**/api/auth/login/', {
            statusCode: 200,
            body: validToken,
        }).as('loginRequest');

        cy.visit('/login');
        cy.get('input[name="email"]').type('valid-email@example.com');
        cy.get('input[name="password"]').type('correct-password');
        cy.get('form').submit();

        cy.wait('@loginRequest').its('response.statusCode').should('eq', 200);
        cy.get('app-custom-snack-bar > .mat-mdc-snack-bar-label').should('contain', 'Logged in successfully');

        cy.url().should('include', '/cars');
        cy.get('h1').should('contain', 'Cars');
    });
});