import { Component, Input, OnInit, input } from '@angular/core';
import { GiftcardService } from '../../services/giftcard.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-gift-card-popup',
  templateUrl: './gift-card-popup.component.html',
  styleUrls: ['./gift-card-popup.component.scss']
})
export class GiftCardPopupComponent{
  email: string;
  isModalOpen: boolean = true;

  constructor(private giftCardService: GiftcardService, private router: Router) {}

  onSubmit(event: Event): void {
    event.preventDefault();
    const formData = new FormData();
    formData.append('name', this.email);
    console.log('email:', this.email);
    const giftCardIdString = localStorage.getItem('giftCardId');
    let giftCardId: number=0;
   if (giftCardIdString !== null) {
     giftCardId = parseInt(giftCardIdString);
     console.log('id ',giftCardId)
     this.giftCardService.sendGiftCard(giftCardId,this.email).subscribe(
      (result) => {
        console.log('Gift card sent successfully:', result);
        this.isModalOpen = false;
        alert(result.message);
        this.router.navigateByUrl('/gift-cards');
      },
      (error) => {
        console.error('error while sending gift card:', error);
        this.isModalOpen = false;
        alert(error.error.message);
      }
    );
     localStorage.removeItem('giftCardId');
   }
    
  }
}
