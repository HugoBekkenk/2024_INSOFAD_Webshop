import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PromocodeAddComponent } from './promocode-add.component';

describe('PromocodeAddComponent', () => {
  let component: PromocodeAddComponent;
  let fixture: ComponentFixture<PromocodeAddComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PromocodeAddComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PromocodeAddComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
