import { TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';

import { AppComponent } from './app.component';

describe('AppComponent', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AppComponent],
      providers: [provideRouter([])],
    }).compileComponents();
  });

  it('should create the app shell', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it('shows navigation entries for the asset flows', () => {
    const fixture = TestBed.createComponent(AppComponent);
    fixture.detectChanges();
    const links = Array.from(
      fixture.nativeElement.querySelectorAll('.nav a') as NodeListOf<Element>,
      (el) => el.textContent?.trim() ?? '',
    );

    expect(links).toEqual(['Assets', 'Create', 'Lookup', 'Me']);
  });
});
