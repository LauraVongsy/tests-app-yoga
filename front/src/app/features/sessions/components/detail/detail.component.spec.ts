import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule, } from '@angular/router/testing';
import { expect } from '@jest/globals'; 
import { SessionService } from '../../../../services/session.service';

import { DetailComponent } from './detail.component';
import { SessionApiService } from '../../services/session-api.service';
import { TeacherService } from 'src/app/services/teacher.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Session } from '../../interfaces/session.interface';
import { of } from 'rxjs';
import { Teacher } from 'src/app/interfaces/teacher.interface';
import { MatCard, MatCardMdImage, MatCardModule } from '@angular/material/card';
import { MatIcon, MatIconModule } from '@angular/material/icon';


describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>; 
  let service: SessionService;
  let sessionApiService: SessionApiService;
  let teacherService: TeacherService;
  let router: Router;
  let matSnackBar: MatSnackBar;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  }

  const mockSessionApiService = {
    detail: jest.fn().mockReturnValue(of({
      id: 1,
      name: 'Test Session',
      date: (() => {
        const d = new Date();
        d.setSeconds(0); // Supprime les secondes
        d.setMilliseconds(0); // Supprime les millisecondes
        return d;
      })(),      description: 'Test Description',
      teacher_id: 123,
      users: [1, 2, 3]
    } as Session)),
    delete: jest.fn().mockReturnValue(of({})),
    participate: jest.fn().mockReturnValue(of({})),
    unParticipate: jest.fn().mockReturnValue(of({}))
  };

  const mockTeacherService = {
    detail: jest.fn().mockReturnValue(of({
      id: 123,
      lastName: 'Doe',
      firstName:'John',
    } as Teacher))
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'sessions', component: DetailComponent }]),
        HttpClientModule,
        MatSnackBarModule,
        MatIconModule,
        MatCardModule,
        ReactiveFormsModule
      ],
      declarations: [DetailComponent], 
      providers: [{ provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: TeacherService, useValue: mockTeacherService },
        { provide: ActivatedRoute, useValue: { snapshot: { paramMap: { get: () => 1 } } } }],
    })
      .compileComponents();
    service = TestBed.inject(SessionService);
    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    sessionApiService = TestBed.inject(SessionApiService);
    teacherService = TestBed.inject(TeacherService);
    router = TestBed.inject(Router);
    matSnackBar = TestBed.inject(MatSnackBar);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch session details on init', () => {
    expect(sessionApiService.detail).toHaveBeenCalledWith(1);
    expect(teacherService.detail).toHaveBeenCalledWith("123");
  
    expect(component.session).toBeDefined();
    expect(component.session!.id).toBe(1);
    expect(component.session!.name).toBe('Test Session');
    expect(component.session!.description).toBe('Test Description');
    expect(component.session!.teacher_id).toBe(123);
    expect(component.session!.users).toEqual([1, 2, 3]);
  
    // Comparer la date sans les millisecondes
    const expectedDate = new Date();
    expectedDate.setSeconds(0);
    expectedDate.setMilliseconds(0);
    const actualDate = new Date(component.session!.date);
    actualDate.setSeconds(0);
    actualDate.setMilliseconds(0);
  
    expect(actualDate.getTime()).toBe(expectedDate.getTime());
  
    expect(component.teacher).toEqual({
      id: 123,
      lastName: 'Doe',
      firstName: 'John',
    });
  
    expect(component.isParticipate).toBe(true);
  });
  

});

