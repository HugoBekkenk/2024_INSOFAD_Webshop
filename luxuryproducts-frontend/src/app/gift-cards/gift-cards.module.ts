import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GiftCardsComponent } from './gift-cards.component';
import { GiftCardThumnailComponent } from './gift-card-thumnail/gift-card-thumnail.component';
import { GiftCardDetailComponent } from './gift-card-detail/gift-card-detail.component';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';



@NgModule({
  declarations: [
    GiftCardsComponent,
    GiftCardThumnailComponent,
    GiftCardDetailComponent
   
  ],
  imports: [
    CommonModule,
    RouterModule,
    FormsModule
  ],
  exports: [
    GiftCardsComponent 
  ]
})
export class GiftCardsModule { }
