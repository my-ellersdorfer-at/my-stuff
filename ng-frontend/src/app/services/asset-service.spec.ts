import { provideHttpClient } from '@angular/common/http';
import {
  HttpTestingController,
  provideHttpClientTesting,
} from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { AssetService } from './asset-service';

describe('AssetService', () => {
  let service: AssetService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        AssetService,
        provideHttpClient(),
        provideHttpClientTesting(),
      ],
    });

    service = TestBed.inject(AssetService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('lists assets via /api/assets/list', () => {
    const assets = [
      { id: '1', name: 'One', owner: 'alice' },
      { id: '2', name: 'Two', owner: 'bob' },
    ];

    service.listAssets().subscribe((result) => expect(result).toEqual(assets));

    const request = httpMock.expectOne('http://localhost:8080/api/assets/list');
    expect(request.request.method).toBe('GET');
    request.flush({ assets });
  });

  it('finds a single asset by id', () => {
    const asset = { id: '42', name: 'Meaning', owner: 'douglas' };

    service.getAsset('42').subscribe((result) => expect(result).toEqual(asset));

    const request = httpMock.expectOne('http://localhost:8080/api/assets/42');
    expect(request.request.method).toBe('GET');
    request.flush(asset);
  });

  it('creates an asset and returns the id', () => {
    service.createAsset().subscribe((result) => expect(result).toBe('1234'));

    const request = httpMock.expectOne(
      'http://localhost:8080/api/assets/create',
    );
    expect(request.request.method).toBe('PUT');
    request.flush('1234');
  });
});
