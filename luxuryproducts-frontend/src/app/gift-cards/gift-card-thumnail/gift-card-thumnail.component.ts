import {Component, EventEmitter, Input, Output} from '@angular/core';

import {GiftCard} from '../../models/giftcard.model';

@Component({
  selector: 'app-gift-card-thumnail',
  templateUrl: './gift-card-thumnail.component.html',
  styleUrl: './gift-card-thumnail.component.scss'
})
export class GiftCardThumnailComponent {
  
  @Input() public giftCard!: GiftCard;
  @Output() public onBuyGiftCard: EventEmitter<GiftCard> = new EventEmitter<GiftCard>();

  public buyGiftCard(giftCard: GiftCard) {
    this.onBuyGiftCard.emit(giftCard);
  }
}
