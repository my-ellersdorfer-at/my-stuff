import { Routes } from '@angular/router';
import { Me } from './me/me';
import { AssetListComponent } from './assets/asset-list.component';
import { AssetCreateComponent } from './assets/asset-create.component';
import { AssetDetailComponent } from './assets/asset-detail.component';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'assets/list',
    pathMatch: 'full',
  },
  {
    path: 'me',
    loadComponent: () => Me,
  },
  {
    path: 'assets',
    children: [
      { path: 'list', loadComponent: () => AssetListComponent },
      { path: 'create', loadComponent: () => AssetCreateComponent },
      { path: 'detail', loadComponent: () => AssetDetailComponent },
      { path: '', redirectTo: 'list', pathMatch: 'full' },
    ],
  },
];
