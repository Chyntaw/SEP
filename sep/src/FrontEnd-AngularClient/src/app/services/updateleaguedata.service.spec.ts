import { TestBed } from '@angular/core/testing';

import { UpdateleaguedataService } from './updateleaguedata.service';

describe('UpdateleaguedataService', () => {
  let service: UpdateleaguedataService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UpdateleaguedataService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
