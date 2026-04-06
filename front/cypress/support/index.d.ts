declare namespace Cypress {
    interface Chainable {
        login(roles?: string[]): void;
        mockCars(): void;
        mockCarsPagination(totalElements: number, pageSize?: number): void;
        mockCar(id: string, brand: string, model: string): void;
        saveCar(): void;
        editCar(id: string): void;
        deleteCar(id: string): void;
    }

}