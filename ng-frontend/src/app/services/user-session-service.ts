import { computed, effect, inject, Injectable } from '@angular/core';
import { rxResource, toObservable } from '@angular/core/rxjs-interop';
import {
  AuthenticationControllerService,
  UserSession,
  UserSessionControllerService,
} from '../../generated';
import { AssetBearerTokenService } from './asset-bearer-token.service';
import { take } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class UserSessionService {
  private readonly userSessionController = inject(UserSessionControllerService);
  private readonly authenticationController = inject(
    AuthenticationControllerService,
  );
  private readonly assetBearerTokenService = inject(AssetBearerTokenService);

  private readonly anonymousUserSession: UserSession = {
    userName: 'anonymous',
    avatar: '--',
    firstName: 'anonymous',
    lastName: 'anonymous',
  };

  private readonly userSessionResource = rxResource({
    defaultValue: this.anonymousUserSession,
    stream: () => {
      return this.userSessionController.getCurrentUserSession();
    },
  });

  readonly userSession = this.userSessionResource.value;

  readonly loggedIn = computed(() =>
    UserSessionService.isLoggedIn(this.userSession()),
  );

  static isLoggedIn(user: UserSession) {
    return user.userName !== 'anonymous';
  }

  readonly isLoading = this.userSessionResource.isLoading;

  readonly loading$ = toObservable(this.userSessionResource.isLoading);

  readonly assetApiAuthenticated = effect(() => {
    if (this.loggedIn()) {
      this.aquireBearerToken();
    }
  });

  private aquireBearerToken() {
    this.authenticationController
      .token()
      .pipe(take(1))
      .subscribe((response) => {
        this.assetBearerTokenService.setBearerToken(response);
      });
  }
}
