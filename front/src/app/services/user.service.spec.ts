import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { expect } from '@jest/globals';

import { UserService } from './user.service';
import { User } from '../interfaces/user.interface';

describe('UserService', () => {
  let service: UserService;
  let httpMock: HttpTestingController;

  const mockUser: User = {
    id: 1,
    firstName: 'John',
    lastName: 'Doe',
    email: 'john.doe@example.com',
    admin: false,
    createdAt: new Date(),
    password: 'password'
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [UserService]
    });
    service = TestBed.inject(UserService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify(); // Ensures no unmatched HTTP requests
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should fetch user by ID', () => {
    service.getById('1').subscribe(user => {
      expect(user).toEqual(mockUser);
    });

    const req = httpMock.expectOne('api/user/1');
    expect(req.request.method).toBe('GET');
    req.flush(mockUser);
  });

  it('should delete user by ID', () => {
    service.delete('1').subscribe(response => {
      expect(response).toBeNull(); // Assume the backend returns no content on successful delete
    });

    const req = httpMock.expectOne('api/user/1');
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
  });

  it('should handle 404 error when fetching a non-existent user', () => {
    service.getById('99').subscribe(
      () => fail('Expected an error, but got a response'),
      (error) => {
        expect(error.status).toBe(404);
      }
    );

    const req = httpMock.expectOne('api/user/99');
    req.flush('Not found', { status: 404, statusText: 'Not Found' });
  });

  it('should handle 404 error when deleting a non-existent user', () => {
    service.delete('99').subscribe(
      () => fail('Expected an error, but got a response'),
      (error) => {
        expect(error.status).toBe(404);
      }
    );

    const req = httpMock.expectOne('api/user/99');
    req.flush('Not found', { status: 404, statusText: 'Not Found' });
  });
});
