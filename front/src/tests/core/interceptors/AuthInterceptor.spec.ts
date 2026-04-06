import { TestBed } from "@angular/core/testing";
import { HttpClient, provideHttpClient, withInterceptorsFromDi } from "@angular/common/http";
import { HttpTestingController, provideHttpClientTesting } from "@angular/common/http/testing";
import { HTTP_INTERCEPTORS } from "@angular/common/http";
import { AuthInterceptor } from "../../../app/core/interceptors/AuthInterceptor";
import { TokenStorageService } from "../../../app/stores/TokenStorageService";
import { NotificationService } from "../../../app/shared/services/NotificationService";
import { Router } from "@angular/router";

describe('AuthInterceptor', () => {
    let httpMock: HttpTestingController;
    let httpClient: HttpClient;
    let tokenService: { getToken: ReturnType<typeof vi.fn>, removeToken: ReturnType<typeof vi.fn> };
    let notificationService: { add: ReturnType<typeof vi.fn> };
    let router: { navigate: ReturnType<typeof vi.fn> };

    beforeEach(() => {
        TestBed.resetTestingModule();

        tokenService = { getToken: vi.fn(), removeToken: vi.fn() };
        notificationService = { add: vi.fn() };
        router = { navigate: vi.fn() };

        TestBed.configureTestingModule({
            providers: [
                provideHttpClient(withInterceptorsFromDi()),
                provideHttpClientTesting(),
                { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
                { provide: TokenStorageService, useValue: tokenService },
                { provide: NotificationService, useValue: notificationService },
                { provide: Router, useValue: router }
            ]
        });

        httpMock = TestBed.inject(HttpTestingController);
        httpClient = TestBed.inject(HttpClient);
    });

    afterEach(() => {
        httpMock.verify();
    });

    describe('when token exists', () => {
        beforeEach(() => {
            tokenService.getToken.mockReturnValue('mock-token');
        });

        it('should add Authorization header', () => {
            httpClient.get('/api/test').subscribe();

            const req = httpMock.expectOne('/api/test');
            expect(req.request.headers.get('Authorization')).toBe('Bearer mock-token');
            req.flush({});
        });

        it('should handle 401 - remove token, notify and redirect to /login', () => {
            httpClient.get('/api/test').subscribe({ error: () => {} });

            const req = httpMock.expectOne('/api/test');
            req.flush({}, { status: 401, statusText: 'Unauthorized' });

            expect(tokenService.removeToken).toHaveBeenCalled();
            expect(notificationService.add).toHaveBeenCalledWith(
                expect.objectContaining({ type: 'error' })
            );
            expect(router.navigate).toHaveBeenCalledWith(['/login']);
        });

        it('should handle 403 - notify and redirect to /forbidden', () => {
            httpClient.get('/api/test').subscribe({ error: () => {} });

            const req = httpMock.expectOne('/api/test');
            req.flush({}, { status: 403, statusText: 'Forbidden' });

            expect(notificationService.add).toHaveBeenCalledWith(
                expect.objectContaining({ type: 'error' })
            );
            expect(router.navigate).toHaveBeenCalledWith(['/forbidden']);
        });

        it('should handle 404 - notify and redirect to /not-found', () => {
            httpClient.get('/api/test').subscribe({ error: () => {} });

            const req = httpMock.expectOne('/api/test');
            req.flush({}, { status: 404, statusText: 'Not Found' });

            expect(notificationService.add).toHaveBeenCalledWith(
                expect.objectContaining({ type: 'error' })
            );
            expect(router.navigate).toHaveBeenCalledWith(['/not-found']);
        });

        it('should handle 409 - notify and redirect to /conflict', () => {
            httpClient.get('/api/test').subscribe({ error: () => {} });

            const req = httpMock.expectOne('/api/test');
            req.flush({}, { status: 409, statusText: 'Conflict' });

            expect(notificationService.add).toHaveBeenCalledWith(
                expect.objectContaining({ type: 'error' })
            );
            expect(router.navigate).toHaveBeenCalledWith(['/conflict']);
        });

        it('should handle unknown error - notify with generic message', () => {
            httpClient.get('/api/test').subscribe({ error: () => {} });

            const req = httpMock.expectOne('/api/test');
            req.flush({}, { status: 500, statusText: 'Internal Server Error' });

            expect(notificationService.add).toHaveBeenCalledWith(
                expect.objectContaining({ type: 'error' })
            );
            expect(router.navigate).not.toHaveBeenCalled();
        });
    });

    describe('when no token', () => {
        beforeEach(() => {
            tokenService.getToken.mockReturnValue(null);
        });

        it('should not add Authorization header', () => {
            httpClient.get('/api/test').subscribe();

            const req = httpMock.expectOne('/api/test');
            expect(req.request.headers.has('Authorization')).toBe(false);
            req.flush({});
        });
    });
});