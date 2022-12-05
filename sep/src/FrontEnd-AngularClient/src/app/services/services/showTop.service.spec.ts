import { TestBed } from '@angular/core/testing';

import { ShowTopService } from './showTop.service';
import {ShowTopComponent} from "../../components/zyklus2/show-top/show-top.component";

describe('ShowTopService', () => {
  let service: ShowTopService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ShowTopService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
