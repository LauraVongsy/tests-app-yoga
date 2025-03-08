import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';

import { expect } from '@jest/globals';

import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { of } from 'rxjs';

import { ListComponent } from './list.component';
import { SessionService } from '../../../../services/session.service';
import { SessionApiService } from '../../services/session-api.service';
import { Session } from '../../interfaces/session.interface';
import { RouterTestingModule } from '@angular/router/testing';

describe('ListComponent', () => {
  let component: ListComponent;
  let fixture: ComponentFixture<ListComponent>;
  let mockSessionService: any;
  let mockSessionApiService: any;

  const mockSessions: Session[] = [
    { id: 1, name: 'Math Session', date: new Date(), teacher_id: 10, description: 'Math class', users: [100, 101] },
    { id: 2, name: 'Science Session', date: new Date(), teacher_id: 20, description: 'Science class', users: [102, 103] }
  ];

  beforeEach(async () => {
    mockSessionService = {
      sessionInformation: { admin: true }
    };

    mockSessionApiService = {
      all: jest.fn().mockReturnValue(of(mockSessions))
    };

    await TestBed.configureTestingModule({
      declarations: [ListComponent],
      imports: [HttpClientTestingModule, MatCardModule, MatIconModule, RouterTestingModule],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessionApiService }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should get sessions from sessionApiService', (done) => {
    component.sessions$.subscribe(sessions => {
      expect(sessions.length).toBe(2);
      expect(sessions).toEqual(mockSessions);
      done();
    });

    expect(mockSessionApiService.all).toHaveBeenCalled();
  });

  it('should return session information from sessionService', () => {
    expect(component.user).toEqual(mockSessionService.sessionInformation);
  });
});
