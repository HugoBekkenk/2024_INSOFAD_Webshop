import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GiftCardPopupComponent } from './gift-card-popup.component';

describe('GiftCardPopupComponent', () => {
  let component: GiftCardPopupComponent;
  let fixture: ComponentFixture<GiftCardPopupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GiftCardPopupComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(GiftCardPopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
