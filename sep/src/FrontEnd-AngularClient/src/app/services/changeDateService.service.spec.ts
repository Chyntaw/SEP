import { TestBed } from '@angular/core/testing';

import { ChangeDateServiceService } from './changeDateService.service';

describe('changeDateServiceService', () => {
  let service: ChangeDateServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ChangeDateServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
