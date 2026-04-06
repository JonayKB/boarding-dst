import { TestBed } from "@angular/core/testing";
import { RouterTestingHarness } from '@angular/router/testing';
import { TokenStorageService } from "../../../app/stores/TokenStorageService";
import { Component } from "@angular/core";
import { provideRouter } from "@angular/router";
import { isAuthenticated, requiredRole } from "../../../app/core/guard/auth.guard";
import { jwtDecode } from "jwt-decode";

@Component({ template: '<h1>Protected Page</h1>' })
class Protected { }

@Component({ template: '<h1>Login Page</h1>' })
class Login { }

@Component({ template: '<h1>Forbidden Page</h1>' })
class Forbidden { }

vi.mock('jwt-decode', () => ({
    jwtDecode: vi.fn()
}));

const VALID_TOKEN_PAYLOAD = { exp: Math.floor(Date.now() / 1000) + 3600, roles: [] as string[] };

describe('authGuard', () => {
    let harness: RouterTestingHarness;

    async function setup(token: string | null) {
        await TestBed.configureTestingModule({
            providers: [
                {
                    provide: TokenStorageService,
                    useValue: {
                        getToken: () => token,
                        setToken: (_token: string) => { },
                        removeToken: () => { },
                    }
                },
                provideRouter([
                    { path: 'protected', component: Protected, canActivate: [isAuthenticated()] },
                    { path: 'login', component: Login },
                    { path: 'admin', component: Protected, canActivate: [requiredRole('ROLE_ADMIN')] },
                    { path: 'forbidden', component: Forbidden }
                ])
            ],
        }).compileComponents();

        harness = await RouterTestingHarness.create();
    }

    afterEach(() => {
        vi.clearAllMocks();
        TestBed.resetTestingModule();
    });

    describe('isAuthenticated', () => {
        it('allows navigation when token is valid', async () => {
            vi.mocked(jwtDecode).mockReturnValue({ ...VALID_TOKEN_PAYLOAD });
            await setup('mock-token');
            await harness.navigateByUrl('/protected', Protected);
            expect(harness.routeNativeElement?.textContent).toContain('Protected Page');
        });

        it('redirects to /login when no token', async () => {
            await setup(null);
            await harness.navigateByUrl('/protected', Login);
            expect(harness.routeNativeElement?.textContent).toContain('Login Page');
        });

        it('redirects to /login when token is expired', async () => {
            vi.mocked(jwtDecode).mockReturnValue({ exp: Math.floor(Date.now() / 1000) - 1, roles: [] });
            await setup('expired-token');
            await harness.navigateByUrl('/protected', Login);
            expect(harness.routeNativeElement?.textContent).toContain('Login Page');
        });
    });

    describe('requiredRole', () => {
        it('allows navigation when user has required role', async () => {
            vi.mocked(jwtDecode).mockReturnValue({ ...VALID_TOKEN_PAYLOAD, roles: ['ROLE_ADMIN'] });
            await setup('mock-token');
            await harness.navigateByUrl('/admin', Protected);
            expect(harness.routeNativeElement?.textContent).toContain('Protected Page');
        });

        it('redirects to /forbidden when user lacks required role', async () => {
            vi.mocked(jwtDecode).mockReturnValue({ ...VALID_TOKEN_PAYLOAD, roles: ['ROLE_USER'] });
            await setup('mock-token');
            await harness.navigateByUrl('/admin', Forbidden);
            expect(harness.routeNativeElement?.textContent).toContain('Forbidden Page');
        });

        it('redirects to /forbidden when no token', async () => {
            await setup(null);
            await harness.navigateByUrl('/admin', Forbidden);
            expect(harness.routeNativeElement?.textContent).toContain('Forbidden Page');
        });
    });
});