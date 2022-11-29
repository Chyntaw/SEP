import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TipprundenuebersichtComponent } from './tipprundenuebersicht.component';

describe('TipprundenuebersichtComponent', () => {
  let component: TipprundenuebersichtComponent;
  let fixture: ComponentFixture<TipprundenuebersichtComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TipprundenuebersichtComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TipprundenuebersichtComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
