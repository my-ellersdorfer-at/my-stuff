import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Me } from './me';
import { provideHttpClient } from '@angular/common/http';
import { provideZonelessChangeDetection } from '@angular/core';
import { provideHttpClientTesting } from '@angular/common/http/testing';

describe('Me', () => {
  let component: Me;
  let fixture: ComponentFixture<Me>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Me],
      providers: [
        provideZonelessChangeDetection(),
        provideHttpClient(),
        provideHttpClientTesting(),
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(Me);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
