import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowTopComponent } from './show-top.component';

describe('ShowTopComponent', () => {
  let component: ShowTopComponent;
  let fixture: ComponentFixture<ShowTopComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ShowTopComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ShowTopComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
