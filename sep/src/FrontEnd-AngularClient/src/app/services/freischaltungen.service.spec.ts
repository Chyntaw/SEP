import { TestBed } from '@angular/core/testing';

import { FreischaltungenService } from './freischaltungen.service';

describe('FreischaltungenService', () => {
  let service: FreischaltungenService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FreischaltungenService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
