import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ZweiFaComponent } from './zwei-fa.component';

describe('ZweiFaComponent', () => {
  let component: ZweiFaComponent;
  let fixture: ComponentFixture<ZweiFaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ZweiFaComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ZweiFaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
