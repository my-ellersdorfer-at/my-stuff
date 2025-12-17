import { Component, inject } from '@angular/core';
import { rxResource } from '@angular/core/rxjs-interop';
import { of } from 'rxjs';

import { AssetService } from '../services/asset-service';
import { UserSessionService } from '../services/user-session-service';

@Component({
  selector: 'app-asset-list',
  standalone: true,
  imports: [],
  templateUrl: './asset-list.component.html',
  styleUrl: './asset-list.component.css',
})
export class AssetListComponent {
  private readonly assetService = inject(AssetService);
  private readonly userSessionService = inject(UserSessionService);

  readonly canUseAssets = this.userSessionService.loggedIn;

  readonly assetsResource = rxResource({
    defaultValue: [],
    stream: () =>
      this.canUseAssets() ? this.assetService.listAssets() : of([]),
  });

  readonly assets = this.assetsResource.value;
  readonly isLoading = this.assetsResource.isLoading;

  refresh() {
    if (!this.canUseAssets()) {
      this.assetsResource.set([]);
      return;
    }
    this.assetsResource.reload();
  }
}
