Cypress.Commands.add('login', (roles: string[] = ['ROLE_USER']) => {
    const header = btoa(JSON.stringify({ alg: 'HS256', typ: 'JWT' }));
    const payload = btoa(JSON.stringify({
        sub: '1234567890',
        roles,
        exp: Math.floor(Date.now() / 1000) + 3600
    }));
    const token = `${header}.${payload}.fake-signature`;

    sessionStorage.setItem('accessToken', token);
});

Cypress.Commands.add('mockCars', () => {

    cy.intercept('GET', '**/v1/cars/**', {
        statusCode: 200,
        body: {
            content: [
                { id: '1', brand: 'Toyota', model: 'Corolla', year: 2020, plate: 'ABC1234' },
                { id: '2', brand: 'Honda', model: 'Civic', year: 2019, plate: 'XYZ5678' }
            ],
            totalPages: 1,
            totalElements: 2,
            size: 10,
            number: 0
        }
    }).as('getCars');
});
Cypress.Commands.add('mockCar', (id: string, brand: string, model: string) => {

    cy.intercept('GET', `**/v1/cars/${id}`, {
        statusCode: 200,
        body: {
            id,
            brand,
            model,
            year: 2020,
            plate: `ABC${id}`
        }
    }).as('getCar');
});

Cypress.Commands.add('mockCarsPagination', (totalElements: number, pageSize: number = 5) => {
    const totalPages = Math.ceil(totalElements / pageSize);

    cy.intercept('GET', '**/v1/cars/**', (req) => {
        const page = parseInt(req.query['page'] as string) || 0;

        const content = Array.from({ length: pageSize }, (_, i) => {
            const index = page * pageSize + i;
            if (index >= totalElements) return null;
            return {
                id: `${index + 1}`,
                brand: `Brand ${index + 1}`,
                model: `Model ${index + 1}`,
                year: 2020,
                plate: `ABC${index + 1}`
            };
        }).filter(Boolean);

        req.reply({
            statusCode: 200,
            body: {
                content,
                totalPages,
                totalElements,
                size: pageSize,
                number: page
            }
        });
    }).as('getCars');
});