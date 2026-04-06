describe('Base Router', () => {
  it('should show login page when accessing /cars', () => {
    cy.visit('/cars');
    cy.url().should('include', '/login');
    cy.get('h1').should('contain', 'Login');
  });

  it('should show login page when accessing /cars/new', () => {
    cy.visit('/cars/new');
    cy.url().should('include', '/login');
    cy.get('h1').should('contain', 'Login');
  });
});
