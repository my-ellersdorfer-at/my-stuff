import { computed, inject, Injectable } from '@angular/core';
import { rxResource, toObservable } from '@angular/core/rxjs-interop';
import { UserSession, UserSessionControllerService } from '../../generated';

@Injectable({ providedIn: 'root' })
export class UserSessionService {
  private readonly userSessionController = inject(UserSessionControllerService);

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
}
