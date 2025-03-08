import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { LoginComponent } from './login.component';
import { AuthService } from '../../services/auth.service';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { LoginRequest } from '../../interfaces/loginRequest.interface';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authService: AuthService;
  let sessionService: SessionService;
  let router: Router;

  const mockSessionInfo: SessionInformation = {
    token: 'mockToken123',
    type: 'Bearer',
    id: 1,
    username: 'john_doe',
    firstName: 'John',
    lastName: 'Doe',
    admin: true
  };

  const mockAuthService = {
    login: jest.fn()
  };

  const mockSessionService = {
    logIn: jest.fn()
  };

  const mockRouter = {
    navigate: jest.fn()
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [ { provide: AuthService, useValue: mockAuthService },
        { provide: SessionService, useValue: mockSessionService },
        { provide: Router, useValue: mockRouter }],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule]
    })
      .compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    authService = TestBed.inject(AuthService);
    sessionService = TestBed.inject(SessionService);
    router = TestBed.inject(Router);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize the form correctly', () => {
    expect(component.form.value).toEqual({ email: '', password: '' });
  });

  it('should call login and navigate on successful login', () => {
    mockAuthService.login.mockReturnValue(of(mockSessionInfo));

    component.form.setValue({ email: 'test@example.com', password: 'password123' });
    component.submit();

    expect(authService.login).toHaveBeenCalledWith({ email: 'test@example.com', password: 'password123' } as LoginRequest);
    expect(sessionService.logIn).toHaveBeenCalledWith(mockSessionInfo);
    expect(router.navigate).toHaveBeenCalledWith(['/sessions']);
  });

  it('should handle login errors correctly', () => {
    mockAuthService.login.mockReturnValue(throwError(() => new Error('Unauthorized')));

    component.form.setValue({ email: 'wrong@example.com', password: 'wrongpass' });
    component.submit();

    expect(component.onError).toBe(true);
  });
});
