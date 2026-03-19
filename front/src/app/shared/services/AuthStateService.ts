import { Injectable, signal, computed } from '@angular/core';
import { TokenData } from '../../types/TokenData';
import { jwtDecode } from 'jwt-decode';
import { TokenStorageService } from '../../stores/TokenStorageService';

@Injectable({ providedIn: 'root' })
export class AuthStateService {
    private _tokenData = signal<TokenData | null>(null);

    readonly tokenData = this._tokenData.asReadonly();
    readonly isLoggedIn = computed(() => this._tokenData() !== null);

    constructor(private tokenService: TokenStorageService) {
        this.loadFromStorage();
    }

    loadFromStorage() {
        const token = this.tokenService.getToken();
        if (token) {
            try {
                this._tokenData.set(jwtDecode<TokenData>(token));
            } catch {
                this._tokenData.set(null);
            }
        }
    }

    setToken(token: string) {
        this.tokenService.setToken(token);
        this._tokenData.set(jwtDecode<TokenData>(token));
    }

    clearToken() {
        this.tokenService.removeToken();
        this._tokenData.set(null);
    }
}