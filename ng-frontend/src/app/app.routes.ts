import { Routes } from '@angular/router';
import { Me } from './me/me';

export const routes: Routes = [
  {
    path: 'me',
    loadComponent: () => Me,
  },
];
