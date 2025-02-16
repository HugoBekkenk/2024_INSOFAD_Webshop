import {Component} from '@angular/core';
import {CartService} from '../services/cart.service';
import {GiftCard} from '../models/giftcard.model';
import {GiftcardService} from '../services/giftcard.service';

@Component({
  selector: 'app-gift-cards',
  templateUrl: './gift-cards.component.html',
  styleUrl: './gift-cards.component.scss'
})
export class GiftCardsComponent {
  
  public giftCards: GiftCard[] = new Array<GiftCard>();
  public loadingGiftCards: boolean = true;

  constructor(private giftCardService: GiftcardService, private cartService: CartService) {
  }

  ngOnInit(): void {
    this.giftCardService
      .getGiftCards()
      .subscribe((giftCards: GiftCard[]) => {
        this.loadingGiftCards = false;
        this.giftCards = giftCards;
      });
  }

  public onBuyGiftCard(giftCard: GiftCard) {
    console.log('Gift card buy in gift cards component');
  }
}
