import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { expect } from '@jest/globals'; 

import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { of } from 'rxjs';

import { MeComponent } from './me.component';
import { SessionService } from '../../services/session.service';
import { UserService } from '../../services/user.service';
import { User } from '../../interfaces/user.interface';

// Import required Angular Material modules
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  let sessionService: SessionService;
  let userService: UserService;
  let router: Router;
  let matSnackBar: MatSnackBar;

  const mockSessionService = {
    sessionInformation: { id: 1, admin: true },
    logOut: jest.fn()
  };

  const mockUserService = {
    getById: jest.fn().mockReturnValue(of({
      id: 1,
      email: 'test@example.com',
      lastName: 'Doe',
      firstName: 'John',
      admin: false,
      password: '',
      createdAt: new Date(),
      updatedAt: new Date()
    } as User)),
    delete: jest.fn().mockReturnValue(of({}))
  };

  const mockRouter = {
    navigate: jest.fn()
  };

  const mockMatSnackBar = {
    open: jest.fn()
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        MatSnackBarModule,
        MatCardModule, 
        MatIconModule  
      ],
      declarations: [MeComponent],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: UserService, useValue: mockUserService },
        { provide: Router, useValue: mockRouter },
        { provide: MatSnackBar, useValue: mockMatSnackBar }
      ]
    }).compileComponents();

    sessionService = TestBed.inject(SessionService);
    userService = TestBed.inject(UserService);
    router = TestBed.inject(Router);
    matSnackBar = TestBed.inject(MatSnackBar);

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch user data on init', () => {
    expect(userService.getById).toHaveBeenCalledWith('1');
    expect(component.user).toBeDefined();
    expect(component.user!.id).toBe(1);
    expect(component.user!.email).toBe('test@example.com');
    expect(component.user!.lastName).toBe('Doe');
    expect(component.user!.firstName).toBe('John');
  });

  it('should navigate back when back() is called', () => {
    jest.spyOn(window.history, 'back');
    component.back();
    expect(window.history.back).toHaveBeenCalled();
  });

  it('should delete user and log out', () => {
    component.delete();

    expect(userService.delete).toHaveBeenCalledWith('1');
    expect(matSnackBar.open).toHaveBeenCalledWith(
      'Your account has been deleted !', 'Close', { duration: 3000 }
    );
    expect(sessionService.logOut).toHaveBeenCalled();
    expect(router.navigate).toHaveBeenCalledWith(['/']);
  });
});
