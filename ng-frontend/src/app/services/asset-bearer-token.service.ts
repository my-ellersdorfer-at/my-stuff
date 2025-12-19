import { effect, Injectable, signal } from '@angular/core';

export const ASSET_BEARER_TOKEN_STORAGE_KEY = 'assetBearerToken';

@Injectable({ providedIn: 'root' })
export class AssetBearerTokenService {
  bearerToken = signal(this.restoreBearerToken());

  constructor() {
    effect(() => this.persistBearerToken(this.bearerToken()));
  }

  public setBearerToken(token: string) {
    this.bearerToken.set(token);
    this.persistBearerToken(token);
  }

  private restoreBearerToken(): string {
    if (typeof sessionStorage === 'undefined') {
      return '';
    }

    return sessionStorage.getItem(ASSET_BEARER_TOKEN_STORAGE_KEY) ?? '';
  }

  private persistBearerToken(token: string) {
    if (typeof sessionStorage === 'undefined') {
      return;
    }

    if (token === '') {
      sessionStorage.removeItem(ASSET_BEARER_TOKEN_STORAGE_KEY);
      return;
    }

    sessionStorage.setItem(ASSET_BEARER_TOKEN_STORAGE_KEY, token);
  }
}
