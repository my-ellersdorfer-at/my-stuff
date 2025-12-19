import {
  ApplicationConfig,
  provideZonelessChangeDetection,
} from '@angular/core';
import { provideRouter } from '@angular/router';
import {
  provideHttpClient,
  withInterceptorsFromDi,
} from '@angular/common/http';

import { routes } from './app.routes';
import { provideAssetBearerTokenInterceptor } from './services/asset-bearer-token.provider';
import { BASE_PATH } from '../generated';

export const API_BASE_PATH = '';

export const appConfig: ApplicationConfig = {
  providers: [
    provideZonelessChangeDetection(),
    provideRouter(routes),
    provideAssetBearerTokenInterceptor(),
    {
      provide: BASE_PATH,
      useValue: API_BASE_PATH,
    },
    provideHttpClient(withInterceptorsFromDi()),
  ],
};
