import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PromocodeEditComponent } from './promocode-edit.component';

describe('PromocodeEditComponent', () => {
  let component: PromocodeEditComponent;
  let fixture: ComponentFixture<PromocodeEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PromocodeEditComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PromocodeEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
