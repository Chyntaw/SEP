import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EinladungsUebersichtComponent } from './einladungs-uebersicht.component';

describe('EinladungsUebersichtComponent', () => {
  let component: EinladungsUebersichtComponent;
  let fixture: ComponentFixture<EinladungsUebersichtComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EinladungsUebersichtComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EinladungsUebersichtComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
