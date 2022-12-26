import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FreischaltungenComponent } from './freischaltungen.component';

describe('FreischaltungenComponent', () => {
  let component: FreischaltungenComponent;
  let fixture: ComponentFixture<FreischaltungenComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FreischaltungenComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FreischaltungenComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
