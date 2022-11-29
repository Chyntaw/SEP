import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SystemdatumComponent } from './systemdatum.component';

describe('SystemdatumComponent', () => {
  let component: SystemdatumComponent;
  let fixture: ComponentFixture<SystemdatumComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SystemdatumComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SystemdatumComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
