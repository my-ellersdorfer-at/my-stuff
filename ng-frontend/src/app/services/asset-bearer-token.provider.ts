import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { AssetBearerTokenInterceptor } from './asset-bearer-token.interceptor';

export function provideAssetBearerTokenInterceptor() {
  return [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AssetBearerTokenInterceptor,
      multi: true,
    },
  ];
}
