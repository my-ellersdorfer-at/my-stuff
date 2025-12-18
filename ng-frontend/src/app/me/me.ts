import { Component, inject } from '@angular/core';
import { UserSessionService } from '../services/user-session-service';

@Component({
  selector: 'app-me',
  imports: [],
  templateUrl: './me.html',
  styleUrl: './me.css',
})
export class Me {
  readonly userSessionService = inject(UserSessionService);

  get session() {
    return this.userSessionService.userSession();
  }

  get handle() {
    return this.session.userName?.trim() || 'anonymous';
  }

  get displayName() {
    const firstName = this.session.firstName?.trim() || '';
    const lastName = this.session.lastName?.trim() || '';
    const fullName = [firstName, lastName].filter(Boolean).join(' ');
    return fullName || this.handle;
  }

  get avatarText() {
    const avatar = this.session.avatar?.trim();
    if (avatar) {
      return avatar;
    }

    const firstInitial = this.session.firstName?.trim()?.[0] ?? '';
    const lastInitial = this.session.lastName?.trim()?.[0] ?? '';
    const initials = `${firstInitial}${lastInitial}`.toUpperCase();
    return initials || '??';
  }

  valueOrPlaceholder(value?: string, placeholder = 'not set') {
    return value?.trim() || placeholder;
  }
}
