import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TippworldheaderComponent } from './tippworldheader.component';

describe('TippworldheaderComponent', () => {
  let component: TippworldheaderComponent;
  let fixture: ComponentFixture<TippworldheaderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TippworldheaderComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TippworldheaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
