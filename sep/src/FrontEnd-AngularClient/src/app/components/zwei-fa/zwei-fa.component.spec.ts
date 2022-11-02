import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ZweiFAComponent } from './zwei-fa.component';

describe('ZweiFAComponent', () => {
  let component: ZweiFAComponent;
  let fixture: ComponentFixture<ZweiFAComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ZweiFAComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ZweiFAComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
