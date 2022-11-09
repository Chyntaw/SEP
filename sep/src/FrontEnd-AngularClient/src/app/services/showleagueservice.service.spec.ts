import { TestBed } from '@angular/core/testing';

import { ShowleagueserviceService } from './showleagueservice.service';

describe('ShowleagueserviceService', () => {
  let service: ShowleagueserviceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ShowleagueserviceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
