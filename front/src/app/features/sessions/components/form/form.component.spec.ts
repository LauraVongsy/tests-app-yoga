import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { expect } from '@jest/globals'; 

import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { ActivatedRoute, Router } from '@angular/router';
import { of } from 'rxjs';
import { FormComponent } from './form.component';
import { SessionService } from '../../../../services/session.service';
import { SessionApiService } from '../../services/session-api.service';
import { TeacherService } from '../../../../services/teacher.service';
import { Session } from '../../interfaces/session.interface';

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let mockSessionService: any;
  let mockSessionApiService: any;
  let mockTeacherService: any;
  let mockRouter: any;
  let mockActivatedRoute: any;
  let mockMatSnackBar: any;

  const mockSession: Session = {
    id: 1,
    name: 'Test Session',
    date: new Date(),
    teacher_id: 5,
    description: 'Test Description',
    users: [100, 101]
  };

  beforeEach(async () => {
    mockSessionService = {
      sessionInformation: { admin: true }
    };

    mockSessionApiService = {
      detail: jest.fn().mockReturnValue(of(mockSession)),
      create: jest.fn().mockReturnValue(of(mockSession)),
      update: jest.fn().mockReturnValue(of(mockSession))
    };

    mockTeacherService = {
      all: jest.fn().mockReturnValue(of([{ id: 5, firstName: 'John', lastName: 'Doe' }]))
    };

    mockRouter = {
      navigate: jest.fn(),
      url: '/sessions'
    };

    mockActivatedRoute = {
      snapshot: { paramMap: { get: jest.fn().mockReturnValue('1') } }
    };

    mockMatSnackBar = {
      open: jest.fn()
    };

    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientTestingModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: TeacherService, useValue: mockTeacherService },
        { provide: Router, useValue: mockRouter },
        { provide: ActivatedRoute, useValue: mockActivatedRoute },
        { provide: MatSnackBar, useValue: mockMatSnackBar }
      ],
      declarations: [FormComponent]
    }).compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should redirect to /sessions if user is not admin', () => {
    mockRouter.url = '/sessions';
    mockSessionService.sessionInformation.admin = false; 

    component.ngOnInit();

    fixture.detectChanges();
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/sessions']);
  });

  it('should initialize form for new session', () => {
    mockRouter.url = '/sessions/create';
    mockActivatedRoute.snapshot.paramMap.get = jest.fn().mockReturnValue(null); 
    fixture.detectChanges();

    expect(component.sessionForm).toBeDefined();
    expect(component.onUpdate).toBe(false);
    expect(component.sessionForm?.value).toEqual({
      name: '',
      date: '',
      teacher_id: '',
      description: ''
    });
  });

  it('should initialize form with session data when updating', () => {
    mockRouter.url = '/sessions/update/1';

    component.ngOnInit();

    expect(component.onUpdate).toBe(true);
    expect(component.sessionForm?.value.name).toBe(mockSession.name);
    expect(component.sessionForm?.value.teacher_id).toBe(mockSession.teacher_id);
    expect(component.sessionForm?.value.description).toBe(mockSession.description);
  });

  it('should call create API when submitting a new session', () => {
    component.onUpdate = false;
    component.sessionForm?.setValue({
      name: 'New Session',
      date: '2025-03-10',
      teacher_id: 10,
      description: 'New session description'
    });

    component.submit();

    expect(mockSessionApiService.create).toHaveBeenCalled();
    expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
    expect(mockMatSnackBar.open).toHaveBeenCalledWith('Session created !', 'Close', { duration: 3000 });
  });

  it('should call update API when updating a session', () => {
    component.onUpdate = true;
    Object.defineProperty(component, 'id', { value: '1' });
    component.sessionForm?.setValue({
      name: 'Updated Session',
      date: '2025-03-15',
      teacher_id: 10,
      description: 'Updated session description'
    });

    component.submit();

    expect(mockSessionApiService.update).toHaveBeenCalledWith('1', expect.any(Object));
    expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
    expect(mockMatSnackBar.open).toHaveBeenCalledWith('Session updated !', 'Close', { duration: 3000 });
  });
});
