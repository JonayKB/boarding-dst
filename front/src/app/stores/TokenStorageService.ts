import { Injectable } from '@angular/core';

@Injectable({
    providedIn: 'root'
})
export class TokenStorageService {

    private TOKEN_KEY = 'accessToken';
    constructor() { }
    public setToken(token: string): void {
        sessionStorage.setItem(this.TOKEN_KEY, token);
    }
    public getToken(): string | null {
        return sessionStorage.getItem(this.TOKEN_KEY);
    }
    public removeToken(): void {
        sessionStorage.removeItem(this.TOKEN_KEY);
    }
}