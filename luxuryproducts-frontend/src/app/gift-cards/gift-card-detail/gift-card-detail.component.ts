import {Component, EventEmitter, Input, Output} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {GiftCard} from '../../models/giftcard.model';
import {GiftcardService} from '../../services/giftcard.service';
import {CartService} from '../../services/cart.service';
import { GiftCardPopupComponent } from '../gift-card-popup/gift-card-popup.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-gift-card-detail',
  templateUrl: './gift-card-detail.component.html',
  styleUrl: './gift-card-detail.component.scss'
})
export class GiftCardDetailComponent {
  @Input() public giftCard!: GiftCard;
  private giftCardId: number;

  constructor(
    private activatedRoute: ActivatedRoute,
    private giftCardService: GiftcardService,
    private cartService: CartService,
    private modalService: NgbModal
  ) {
  }

  ngOnInit() {

    this.activatedRoute.params.subscribe(params => {
      this.giftCardId = params['id'];
    });

    this.giftCardService
      .getGiftCardtById(this.giftCardId)
      .subscribe((giftCard: GiftCard) => {
        this.giftCard = giftCard;
      });
  }

  public onBuyGiftCard(giftCard: GiftCard) {
    console.log('buy gift card in gift-card-details');
    localStorage.setItem("giftCardId",this.giftCardId.toString());
    const modalRef = this.modalService.open(GiftCardPopupComponent);
    modalRef.result.then((result) => {
      console.log('The dialog was closed with result:', result);
    }, (reason) => {
      console.log('The dialog was dismissed:', reason);
    });
  }

}
