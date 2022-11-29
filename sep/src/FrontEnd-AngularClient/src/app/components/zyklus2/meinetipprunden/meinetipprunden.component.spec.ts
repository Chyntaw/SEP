import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MeinetipprundenComponent } from './meinetipprunden.component';

describe('MeinetipprundenComponent', () => {
  let component: MeinetipprundenComponent;
  let fixture: ComponentFixture<MeinetipprundenComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MeinetipprundenComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MeinetipprundenComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
