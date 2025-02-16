import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GiftCardThumnailComponent } from './gift-card-thumnail.component';

describe('GiftCardThumnailComponent', () => {
  let component: GiftCardThumnailComponent;
  let fixture: ComponentFixture<GiftCardThumnailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GiftCardThumnailComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(GiftCardThumnailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
