import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GiftCardDetailComponent } from './gift-card-detail.component';

describe('GiftCardDetailComponent', () => {
  let component: GiftCardDetailComponent;
  let fixture: ComponentFixture<GiftCardDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GiftCardDetailComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(GiftCardDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
