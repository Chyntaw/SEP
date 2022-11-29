import { TestBed } from '@angular/core/testing';

import { TipprundenserviceService } from './tipprundenservice.service';

describe('TipprundenserviceService', () => {
  let service: TipprundenserviceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TipprundenserviceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
