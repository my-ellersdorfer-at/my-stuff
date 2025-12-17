import { ComponentFixture, TestBed } from '@angular/core/testing';
import { BehaviorSubject } from 'rxjs';
import { provideZonelessChangeDetection, signal } from '@angular/core';

import { AssetListComponent } from './asset-list.component';
import { AssetService } from '../services/asset-service';
import { UserSessionService } from '../services/user-session-service';

describe('AssetListComponent', () => {
  let fixture: ComponentFixture<AssetListComponent>;
  let assetStream: BehaviorSubject<
    { id: string; name: string; owner: string }[]
  >;
  const loggedIn = signal(true);

  beforeEach(async () => {
    loggedIn.set(true);
    assetStream = new BehaviorSubject([
      { id: '1', name: 'Camera', owner: 'Alice' },
      { id: '2', name: 'Laptop', owner: 'Bob' },
    ]);

    await TestBed.configureTestingModule({
      imports: [AssetListComponent],
      providers: [
        provideZonelessChangeDetection(),
        {
          provide: AssetService,
          useValue: {
            listAssets: () => assetStream.asObservable(),
          },
        },
        {
          provide: UserSessionService,
          useValue: {
            loggedIn,
          },
        },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(AssetListComponent);
    fixture.detectChanges();
  });

  it('renders assets in a table', async () => {
    await fixture.whenStable();
    const rows = fixture.nativeElement.querySelectorAll(
      '[data-testid="asset-row"]',
    );
    expect(rows.length).toBe(2);
    expect(rows[0].textContent).toContain('Camera');
    expect(rows[0].textContent).toContain('Alice');
  });

  it('shows auth warning when not logged in', async () => {
    loggedIn.set(false);
    assetStream.next([
      { id: '1', name: 'Camera', owner: 'Alice' },
      { id: '2', name: 'Laptop', owner: 'Bob' },
    ]);
    fixture.detectChanges();
    await fixture.whenStable();

    const warning = fixture.nativeElement.querySelector(
      '[data-testid="assets-auth-warning"]',
    );
    expect(warning?.textContent).toContain('Sign in');
  });

  it('shows an empty state when there are no assets', async () => {
    loggedIn.set(true);
    fixture.detectChanges();
    assetStream.next([]);
    fixture.detectChanges();
    await fixture.whenStable();

    const emptyState = fixture.nativeElement.querySelector(
      '[data-testid="empty-assets"]',
    );
    expect(emptyState?.textContent).toContain('No assets yet');
  });
});
