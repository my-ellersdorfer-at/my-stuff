import {
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { inject, Injectable } from '@angular/core';
import { AssetBearerTokenService } from './asset-bearer-token.service';

@Injectable({
  providedIn: 'root',
})
export class AssetBearerTokenInterceptor implements HttpInterceptor {
  private readonly assetBearerTokenService = inject(AssetBearerTokenService);

  intercept(
    req: HttpRequest<unknown>,
    next: HttpHandler,
  ): Observable<HttpEvent<unknown>> {
    return next.handle(
      req.clone({
        setHeaders: {
          Authorization: 'Bearer ' + this.assetBearerTokenService.bearerToken(),
        },
      }),
    );
  }
}
