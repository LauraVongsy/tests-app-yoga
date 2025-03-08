import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { expect } from '@jest/globals';

import { SessionApiService } from './session-api.service';
import { Session } from '../interfaces/session.interface';

describe('SessionApiService', () => {
  let service: SessionApiService;
  let httpMock: HttpTestingController;

  const mockSession: Session = {
    id: 1,
    name: 'Math Session',
    date: new Date(),
    teacher_id: 10,
    description: 'This is an advanced math session.',
    users: [100, 101]
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [SessionApiService]
    });
    service = TestBed.inject(SessionApiService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify(); // Ensures no unmatched HTTP requests
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should fetch all sessions', () => {
    const mockSessions: Session[] = [mockSession];

    service.all().subscribe(sessions => {
      expect(sessions.length).toBe(1);
      expect(sessions).toEqual(mockSessions);
    });

    const req = httpMock.expectOne('api/session');
    expect(req.request.method).toBe('GET');
    req.flush(mockSessions);
  });

  it('should fetch session details by ID', () => {
    service.detail('1').subscribe(session => {
      expect(session).toEqual(mockSession);
    });

    const req = httpMock.expectOne('api/session/1');
    expect(req.request.method).toBe('GET');
    req.flush(mockSession);
  });

  it('should create a new session', () => {
    service.create(mockSession).subscribe(session => {
      expect(session).toEqual(mockSession);
    });

    const req = httpMock.expectOne('api/session');
    expect(req.request.method).toBe('POST');
    req.flush(mockSession);
  });

  it('should update a session', () => {
    const updatedSession: Session = { ...mockSession, name: 'Updated Math Session' };

    service.update('1', updatedSession).subscribe(session => {
      expect(session).toEqual(updatedSession);
    });

    const req = httpMock.expectOne('api/session/1');
    expect(req.request.method).toBe('PUT');
    req.flush(updatedSession);
  });

  it('should delete a session', () => {
    service.delete('1').subscribe(response => {
      expect(response).toBeNull(); // Assume backend returns no content
    });

    const req = httpMock.expectOne('api/session/1');
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
  });

  it('should allow user to participate in a session', () => {
    service.participate('1', '100').subscribe(response => {
      expect(response).toBeUndefined();
    });

    const req = httpMock.expectOne('api/session/1/participate/100');
    expect(req.request.method).toBe('POST');
    req.flush(null);
  });

  it('should allow user to unparticipate from a session', () => {
    service.unParticipate('1', '100').subscribe(response => {
      expect(response).toBeUndefined();
    });

    const req = httpMock.expectOne('api/session/1/participate/100');
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
  });

  it('should handle 404 error when fetching a non-existent session', () => {
    service.detail('99').subscribe(
      () => fail('Expected an error, but got a response'),
      (error) => {
        expect(error.status).toBe(404);
      }
    );

    const req = httpMock.expectOne('api/session/99');
    req.flush('Not found', { status: 404, statusText: 'Not Found' });
  });

  it('should handle 404 error when deleting a non-existent session', () => {
    service.delete('99').subscribe(
      () => fail('Expected an error, but got a response'),
      (error) => {
        expect(error.status).toBe(404);
      }
    );

    const req = httpMock.expectOne('api/session/99');
    req.flush('Not found', { status: 404, statusText: 'Not Found' });
  });
});
