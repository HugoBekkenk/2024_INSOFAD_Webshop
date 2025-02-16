import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PromocodeAdminComponent } from './promocode-admin.component';

describe('PromocodeAdminComponent', () => {
  let component: PromocodeAdminComponent;
  let fixture: ComponentFixture<PromocodeAdminComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PromocodeAdminComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PromocodeAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
