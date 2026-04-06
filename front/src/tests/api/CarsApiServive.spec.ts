import { TestBed } from "@angular/core/testing";
import { CarsApiService } from "../../app/api/CarsApiService";
import { HttpTestingController, provideHttpClientTesting } from "@angular/common/http/testing";
import { provideHttpClient } from "@angular/common/http";
import { NotificationService } from "../../app/shared/services/NotificationService";
import { environment } from "../../environments/environment";

const API = environment.apiUrl;

describe('CarsApiService', () => {
    let service: CarsApiService;
    let httpMock: HttpTestingController;
    let notificationService: { add: ReturnType<typeof vi.fn> };

    beforeEach(() => {
        TestBed.resetTestingModule();

        notificationService = { add: vi.fn() };

        TestBed.configureTestingModule({
            providers: [
                CarsApiService,
                provideHttpClient(),
                provideHttpClientTesting(),
                { provide: NotificationService, useValue: notificationService }
            ]
        });

        httpMock = TestBed.inject(HttpTestingController);
        service = TestBed.inject(CarsApiService);
    });

    afterEach(() => {
        httpMock.verify();
    });

    // ─── getCars ────────────────────────────────────────────────────────────────

    describe('getCars', () => {
        it('should fetch cars with correct parameters', () => {
            const mockResponse = {
                content: [
                    { id: '1', brand: 'Toyota', model: 'Corolla', year: 2020, plate: 'ABC1234' },
                    { id: '2', brand: 'Honda', model: 'Civic', year: 2019, plate: 'XYZ5678' }
                ],
                totalPages: 1, totalElements: 2, size: 10, number: 0
            };

            service.getCars(0, { brand: 'Toyota' }, { sortBy: 'year', asc: true }).subscribe(response => {
                expect(response).toEqual(mockResponse);
            });

            const req = httpMock.expectOne(r =>
                r.url.endsWith('/v1/cars/') &&
                r.params.get('page') === '0' &&
                r.params.get('brand') === 'Toyota' &&
                r.params.get('sortBy') === 'year' &&
                r.params.get('asc') === 'true'
            );
            expect(req.request.method).toBe('GET');
            req.flush(mockResponse);
        });

        it('should call notificationService on error', () => {
            service.getCars().subscribe({
                next: () => { throw new Error('Expected error') },
                error: (error) => {
                    expect(error.status).toBe(500);
                    expect(notificationService.add).toHaveBeenCalledWith(
                        expect.objectContaining({ type: 'error' })
                    );
                }
            });

            const req = httpMock.expectOne(r => r.url.endsWith('/v1/cars/'));
            req.flush({ message: 'Internal Server Error' }, { status: 500, statusText: 'Internal Server Error' });
        });
    });

    // ─── saveCars ───────────────────────────────────────────────────────────────

    describe('saveCars', () => {
        const saveCar = { brand: 'Ford', model: 'Focus', year: 2018, plate: 'DEF5678' };

        it('should save a car and return response text', () => {
            service.saveCars(saveCar).subscribe(response => {
                expect(response).toBe('Car saved successfully');
            });

            const req = httpMock.expectOne(r =>
                r.url.endsWith('/v1/cars/') &&
                r.method === 'POST'
            );
            expect(req.request.body).toEqual(saveCar);
            req.flush('Car saved successfully');
        });

        it('should call notificationService on error', () => {
            service.saveCars(saveCar).subscribe({
                next: () => { throw new Error('Expected error') },
                error: (error) => {
                    expect(error.status).toBe(500);
                    expect(notificationService.add).toHaveBeenCalledWith(
                        expect.objectContaining({ type: 'error' })
                    );
                }
            });

            const req = httpMock.expectOne(r => r.url.endsWith('/v1/cars/') && r.method === 'POST');
            req.flush({ message: 'Internal Server Error' }, { status: 500, statusText: 'Internal Server Error' });
        });
    });

    // ─── getCarById ─────────────────────────────────────────────────────────────

    describe('getCarById', () => {
        const mockCar = { id: '1', brand: 'Toyota', model: 'Corolla', year: 2020, plate: 'ABC1234' };

        it('should fetch a car by id', () => {
            service.getCarById('1').subscribe(response => {
                expect(response).toEqual(mockCar);
            });

            const req = httpMock.expectOne(`${API}/v1/cars/1/`);
            expect(req.request.method).toBe('GET');
            req.flush(mockCar);
        });

        it('should call notificationService on error', () => {
            service.getCarById('1').subscribe({
                next: () => { throw new Error('Expected error') },
                error: (error) => {
                    expect(error.status).toBe(404);
                    expect(notificationService.add).toHaveBeenCalledWith(
                        expect.objectContaining({ type: 'error' })
                    );
                }
            });

            const req = httpMock.expectOne(`${API}/v1/cars/1/`);
            req.flush({ message: 'Not Found' }, { status: 404, statusText: 'Not Found' });
        });
    });

    // ─── updateCar ──────────────────────────────────────────────────────────────

    describe('updateCar', () => {
        const saveCar = { brand: 'Ford', model: 'Focus', year: 2018, plate: 'DEF5678' };

        it('should update a car and return response text', () => {
            service.updateCar('1', saveCar).subscribe(response => {
                expect(response).toBe('Car updated successfully');
            });

            const req = httpMock.expectOne(`${API}/v1/cars/1/`);
            expect(req.request.method).toBe('PUT');
            expect(req.request.body).toEqual(saveCar);
            req.flush('Car updated successfully');
        });

        it('should call notificationService on error', () => {
            service.updateCar('1', saveCar).subscribe({
                next: () => { throw new Error('Expected error') },
                error: (error) => {
                    expect(error.status).toBe(500);
                    expect(notificationService.add).toHaveBeenCalledWith(
                        expect.objectContaining({ type: 'error' })
                    );
                }
            });

            const req = httpMock.expectOne(`${API}/v1/cars/1/`);
            req.flush({ message: 'Internal Server Error' }, { status: 500, statusText: 'Internal Server Error' });
        });
    });

    // ─── deleteCar ──────────────────────────────────────────────────────────────

    describe('deleteCar', () => {
        it('should delete a car and return response text', () => {
            service.deleteCar('1').subscribe(response => {
                expect(response).toBe('Car deleted successfully');
            });

            const req = httpMock.expectOne(`${API}/v1/cars/1/`);
            expect(req.request.method).toBe('DELETE');
            req.flush('Car deleted successfully');
        });

        it('should call notificationService on error', () => {
            service.deleteCar('1').subscribe({
                next: () => { throw new Error('Expected error') },
                error: (error) => {
                    expect(error.status).toBe(500);
                    expect(notificationService.add).toHaveBeenCalledWith(
                        expect.objectContaining({ type: 'error' })
                    );
                }
            });

            const req = httpMock.expectOne(`${API}/v1/cars/1/`);
            req.flush({ message: 'Internal Server Error' }, { status: 500, statusText: 'Internal Server Error' });
        });
    });
});