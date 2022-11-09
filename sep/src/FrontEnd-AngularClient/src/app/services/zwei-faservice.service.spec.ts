import { TestBed } from '@angular/core/testing';

import {ZweiFaserviceService} from "./zwei-faservice.service";

describe('ZweiFaserviceService', () => {
  let service: ZweiFaserviceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ZweiFaserviceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
