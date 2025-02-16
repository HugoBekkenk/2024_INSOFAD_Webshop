import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { GiftCardPopupComponent } from './gift-card-popup.component';

@NgModule({
  declarations: [
    GiftCardPopupComponent
  ],
  imports: [
    CommonModule,
    FormsModule
  ],
  exports:[GiftCardPopupComponent]
})
export class GiftCardPopupModule { }
