import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent {
  readonly navItems = [
    { label: 'Assets', route: '/assets/list' },
    { label: 'Create', route: '/assets/create' },
    { label: 'Lookup', route: '/assets/detail' },
    { label: 'Me', route: '/me' },
  ];
}
