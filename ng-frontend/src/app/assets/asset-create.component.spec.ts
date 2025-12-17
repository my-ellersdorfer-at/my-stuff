import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Subject } from 'rxjs';
import { provideZonelessChangeDetection, signal } from '@angular/core';

import { AssetCreateComponent } from './asset-create.component';
import { AssetService } from '../services/asset-service';
import { UserSessionService } from '../services/user-session-service';

describe('AssetCreateComponent', () => {
  let fixture: ComponentFixture<AssetCreateComponent>;
  let createSubject: Subject<string>;
  let createSpy: jasmine.Spy;
  const loggedIn = signal(true);

  beforeEach(async () => {
    loggedIn.set(true);
    createSubject = new Subject<string>();
    createSpy = jasmine
      .createSpy()
      .and.returnValue(createSubject.asObservable());

    await TestBed.configureTestingModule({
      imports: [AssetCreateComponent],
      providers: [
        provideZonelessChangeDetection(),
        {
          provide: AssetService,
          useValue: {
            createAsset: createSpy,
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

    fixture = TestBed.createComponent(AssetCreateComponent);
    fixture.detectChanges();
  });

  it('requests asset creation and shows the returned id', async () => {
    const button = fixture.nativeElement.querySelector(
      '[data-testid="create-asset-button"]',
    ) as HTMLButtonElement;

    button.click();
    expect(createSpy).toHaveBeenCalledTimes(1);

    createSubject.next('asset-123');
    createSubject.complete();
    fixture.detectChanges();
    await fixture.whenStable();

    const result = fixture.nativeElement.querySelector(
      '[data-testid="creation-result"]',
    );
    expect(result?.textContent).toContain('asset-123');
  });

  it('disables the button while submitting', () => {
    const button = fixture.nativeElement.querySelector(
      '[data-testid="create-asset-button"]',
    ) as HTMLButtonElement;

    button.click();
    fixture.detectChanges();

    expect(button.disabled).toBeTrue();
  });

  it('blocks creation when not authenticated', () => {
    loggedIn.set(false);

    const button = fixture.nativeElement.querySelector(
      '[data-testid="create-asset-button"]',
    ) as HTMLButtonElement;

    button.click();
    fixture.detectChanges();

    expect(createSpy).not.toHaveBeenCalled();
    const error = fixture.nativeElement.querySelector(
      '[data-testid="creation-error"]',
    );
    expect(error?.textContent).toContain('sign in');
  });
});
