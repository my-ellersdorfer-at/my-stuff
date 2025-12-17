import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of, throwError } from 'rxjs';
import { provideZonelessChangeDetection, signal } from '@angular/core';
import { By } from '@angular/platform-browser';

import { AssetDetailComponent } from './asset-detail.component';
import { AssetService } from '../services/asset-service';
import { UserSessionService } from '../services/user-session-service';

describe('AssetDetailComponent', () => {
  let fixture: ComponentFixture<AssetDetailComponent>;
  let getAssetSpy: jasmine.Spy;
  const loggedIn = signal(true);

  beforeEach(async () => {
    loggedIn.set(true);
    getAssetSpy = jasmine
      .createSpy()
      .and.returnValue(of({ id: '3', name: 'Tripod', owner: 'Sam' }));

    await TestBed.configureTestingModule({
      imports: [AssetDetailComponent],
      providers: [
        provideZonelessChangeDetection(),
        {
          provide: AssetService,
          useValue: {
            getAsset: getAssetSpy,
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

    fixture = TestBed.createComponent(AssetDetailComponent);
    fixture.detectChanges();
  });

  it('fetches and displays an asset by id', async () => {
    loggedIn.set(true);
    fixture.detectChanges();
    const input: HTMLInputElement = fixture.nativeElement.querySelector(
      'input[name="assetId"]',
    );
    input.value = '3';
    input.dispatchEvent(new Event('input'));

    const form = fixture.nativeElement.querySelector('form');
    form.dispatchEvent(new Event('submit'));
    fixture.detectChanges();
    await fixture.whenStable();

    expect(getAssetSpy).toHaveBeenCalledWith('3');
    const panel = fixture.nativeElement.querySelector(
      '[data-testid="asset-detail"]',
    );
    expect(panel?.textContent).toContain('Tripod');
    expect(panel?.textContent).toContain('Sam');
  });

  it('shows a helpful message when the lookup fails', async () => {
    loggedIn.set(true);
    fixture.detectChanges();
    getAssetSpy.and.returnValue(throwError(() => new Error('not found')));

    const input: HTMLInputElement = fixture.nativeElement.querySelector(
      'input[name="assetId"]',
    );
    input.value = 'missing';
    input.dispatchEvent(new Event('input'));
    fixture.nativeElement
      .querySelector('form')
      .dispatchEvent(new Event('submit'));
    fixture.detectChanges();
    await fixture.whenStable();

    const message = fixture.debugElement.query(
      By.css('[data-testid="asset-error"]'),
    )?.nativeElement;
    expect(message?.textContent).toContain('Could not find asset');
  });

  it('blocks lookup when not authenticated', async () => {
    loggedIn.set(false);

    const form = fixture.nativeElement.querySelector('form');
    form.dispatchEvent(new Event('submit'));
    fixture.detectChanges();
    await fixture.whenStable();

    expect(getAssetSpy).not.toHaveBeenCalled();
    const message = fixture.debugElement.query(
      By.css('[data-testid="asset-error"]'),
    )?.nativeElement;
    expect(message?.textContent).toContain('sign in');
  });
});
