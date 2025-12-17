import { inject, Injectable } from '@angular/core';
import { map } from 'rxjs';
import { AssetControllerService } from '../../generated';

@Injectable({ providedIn: 'root' })
export class AssetService {
  private readonly controller = inject(AssetControllerService);

  listAssets() {
    return this.controller.list().pipe(map((payload) => payload.assets ?? []));
  }

  getAsset(id: string) {
    return this.controller.find(id);
  }

  createAsset() {
    return this.controller.create('body', false, {
      // Backend returns plain text id; request text to avoid JSON parse errors.
      httpHeaderAccept: 'text/plain' as unknown as 'application/json',
    });
  }
}
