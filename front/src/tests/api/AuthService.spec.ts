import { TestBed } from "@angular/core/testing";
import { AuthService } from "../../app/api/AuthService";
import { provideHttpClient } from "@angular/common/http";
import { provideHttpClientTesting, HttpTestingController } from "@angular/common/http/testing";
import { NotificationService } from "../../app/shared/services/NotificationService";

describe('AuthService', () => {
    let service: AuthService;
    let httpMock: HttpTestingController;
    let notificationService: { add: ReturnType<typeof vi.fn> };

    beforeEach(() => {
        TestBed.resetTestingModule();
        notificationService = { add: vi.fn() };

        TestBed.configureTestingModule({
            providers: [
                AuthService,
                provideHttpClient(),
                provideHttpClientTesting(),
                { provide: NotificationService, useValue: notificationService }
            ],
        });

        service = TestBed.inject(AuthService);
        httpMock = TestBed.inject(HttpTestingController);
    });

    afterEach(() => {
        httpMock.verify();
    });

    describe('login', () => {
        it('should send POST request with email and password', () => {
            const mockToken = 'mock-jwt-token';

            service.login('test@example.com', 'password123').subscribe();

            const req = httpMock.expectOne(r => r.url.endsWith('/api/auth/login/'));
            expect(req.request.method).toBe('POST');
            expect(req.request.body).toEqual({ email: 'test@example.com', password: 'password123' });
            req.flush({ token: mockToken });
        });

        it('should handle login errors and show notification', () => {
            service.login('test@example.com', 'wrongpassword').subscribe({
                error: () => {
                    expect(notificationService.add).toHaveBeenCalledWith(
                        expect.objectContaining({ type: 'error' })
                    );
                }
            });

            const req = httpMock.expectOne(r => r.url.endsWith('/api/auth/login/'));
            expect(req.request.method).toBe('POST');
            req.flush({ error: 'Invalid credentials' }, { status: 401, statusText: 'Unauthorized' });
        });
    });
});