import { Component, inject, signal } from '@angular/core';
import { finalize } from 'rxjs';

import { AssetService } from '../services/asset-service';
import { UserSessionService } from '../services/user-session-service';

@Component({
  selector: 'app-asset-create',
  standalone: true,
  imports: [],
  templateUrl: './asset-create.component.html',
  styleUrl: './asset-create.component.css',
})
export class AssetCreateComponent {
  private readonly assetService = inject(AssetService);
  private readonly userSessionService = inject(UserSessionService);

  readonly isSubmitting = signal(false);
  readonly createdId = signal<string | null>(null);
  readonly error = signal<string | null>(null);
  readonly isLoggedIn = this.userSessionService.loggedIn;

  create() {
    if (!this.userSessionService.loggedIn()) {
      this.error.set('You need to sign in before creating assets.');
      return;
    }

    if (this.isSubmitting()) {
      return;
    }

    this.isSubmitting.set(true);
    this.error.set(null);
    this.assetService
      .createAsset()
      .pipe(finalize(() => this.isSubmitting.set(false)))
      .subscribe({
        next: (id) => this.createdId.set(id),
        error: (error) => {
          console.error(error);
          this.error.set('Could not create asset right now. Try again.');
        },
      });
  }
}
