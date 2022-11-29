import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowLeagueDataComponent } from './show-league-data.component';

describe('ShowLeagueDataComponent', () => {
  let component: ShowLeagueDataComponent;
  let fixture: ComponentFixture<ShowLeagueDataComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ShowLeagueDataComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ShowLeagueDataComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
