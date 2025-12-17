import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { finalize } from 'rxjs';

import { AssetService } from '../services/asset-service';
import { Asset } from '../../generated';
import { UserSessionService } from '../services/user-session-service';

@Component({
  selector: 'app-asset-detail',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './asset-detail.component.html',
  styleUrl: './asset-detail.component.css',
})
export class AssetDetailComponent {
  private readonly fb = inject(FormBuilder);
  private readonly assetService = inject(AssetService);
  private readonly userSessionService = inject(UserSessionService);
  readonly isLoggedIn = this.userSessionService.loggedIn;

  readonly form = this.fb.nonNullable.group({
    assetId: ['', [Validators.required]],
  });

  readonly asset = signal<Asset | null>(null);
  readonly error = signal<string | null>(null);
  readonly loading = signal(false);

  lookup() {
    if (!this.userSessionService.loggedIn()) {
      this.error.set('Please sign in to lookup assets.');
      return;
    }

    if (this.form.invalid) {
      return;
    }

    this.loading.set(true);
    this.error.set(null);
    this.asset.set(null);

    const id = this.form.controls.assetId.value;
    this.assetService
      .getAsset(id)
      .pipe(finalize(() => this.loading.set(false)))
      .subscribe({
        next: (found) => this.asset.set(found),
        error: () => this.error.set('Could not find asset with that id.'),
      });
  }
}
