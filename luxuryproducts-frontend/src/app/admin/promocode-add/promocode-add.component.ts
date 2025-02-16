import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { PromoCodeService } from '../../services/promocode.service';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-promocode-add',
  templateUrl: 'promocode-add.component.html',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    RouterLink
  ],
  styleUrls: ['promocode-add.component.scss']
})
export class PromocodeAddComponent implements OnInit {
  promoCodeForm: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private promoCodeService: PromoCodeService,
    private router: Router
  ) {
    this.promoCodeForm = this.formBuilder.group({
      code: ['', Validators.required],
      discount: ['', [Validators.required, Validators.min(0)]],
      expiryDate: ['', Validators.required],
      maxUsageCount: ['', [Validators.required, Validators.min(1)]],
      type: ['', Validators.required]
    });
  }

  onSubmit(): void {
    if (this.promoCodeForm.invalid) {
      return;
    }

    // Format expiryDate to ISO 8601 string format
    const promoCodeData = { ...this.promoCodeForm.value, expiryDate: new Date(this.promoCodeForm.value.expiryDate).toISOString() };

    this.promoCodeService.createPromoCode(promoCodeData).subscribe(
      () => {
        console.log('Promo code created successfully.');
        this.promoCodeForm.reset();
        this.router.navigate(['/admin-promo-admin']);
      },
      (error: any) => {
        console.error('Error creating promo code:', error);
      }
    );
  }

  ngOnInit(): void {
  }
}
