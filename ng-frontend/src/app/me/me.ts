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
}
