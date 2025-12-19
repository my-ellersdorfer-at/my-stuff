import { TestBed } from '@angular/core/testing';
import {
  ASSET_BEARER_TOKEN_STORAGE_KEY,
  AssetBearerTokenService,
} from './asset-bearer-token.service';

describe('AssetBearerTokenService', () => {
  beforeEach(() => {
    sessionStorage.clear();
    TestBed.configureTestingModule({
      providers: [AssetBearerTokenService],
    });
  });

  it('hydrates the bearer token from session storage on creation', () => {
    sessionStorage.setItem(
      ASSET_BEARER_TOKEN_STORAGE_KEY,
      'token-from-storage',
    );

    const service = TestBed.inject(AssetBearerTokenService);

    expect(service.bearerToken()).toBe('token-from-storage');
  });

  it('persists bearer token updates to session storage', () => {
    const service = TestBed.inject(AssetBearerTokenService);

    service.setBearerToken('persist-me');

    expect(service.bearerToken()).toBe('persist-me');
    expect(sessionStorage.getItem(ASSET_BEARER_TOKEN_STORAGE_KEY)).toBe(
      'persist-me',
    );
  });

  it('removes the persisted bearer token when cleared', () => {
    const service = TestBed.inject(AssetBearerTokenService);

    service.setBearerToken('persist-me');
    service.setBearerToken('');

    expect(service.bearerToken()).toBe('');
    expect(sessionStorage.getItem(ASSET_BEARER_TOKEN_STORAGE_KEY)).toBeNull();
  });
});
