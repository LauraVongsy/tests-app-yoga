import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionService } from './session.service';
import { SessionInformation } from '../interfaces/sessionInformation.interface';

describe('SessionService', () => {
  let service: SessionService;
  let mockUser: SessionInformation;


  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);

    mockUser = {
      token: 'mockToken123',
      type: 'Bearer',
      id: 1,
      username: 'john_doe',
      firstName: 'John',
      lastName: 'Doe',
      admin: true,
    };
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should log in a user and store session info', () => {
    service.logIn(mockUser);

    expect(service.isLogged).toBe(true);
    expect(service.sessionInformation).toEqual(mockUser);
  });

  it('should log out a user and clear session info', () => {
    service.logIn(mockUser);
    service.logOut();

    expect(service.isLogged).toBe(false);
    expect(service.sessionInformation).toBeUndefined();
  });
});
