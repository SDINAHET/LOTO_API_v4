import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EuromillionsComponent } from './euromillions.component';

describe('EuromillionsComponent', () => {
  let component: EuromillionsComponent;
  let fixture: ComponentFixture<EuromillionsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EuromillionsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EuromillionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
