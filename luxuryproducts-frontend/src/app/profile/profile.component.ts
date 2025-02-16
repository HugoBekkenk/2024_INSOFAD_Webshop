import {Component, OnInit} from '@angular/core';
import {UserService} from "../services/user.service";
import {User} from "../models/user.model";
import {CommonModule} from "@angular/common";
import {RouterLink} from "@angular/router";
import { GiftcardService } from '../services/giftcard.service';
import { UserGiftCard } from '../models/usergiftcard.model';
import { GiftCard } from '../models/giftcard.model';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss'
})
export class ProfileComponent implements OnInit {
  public user: User;
  public sentGiftCards: UserGiftCard[] = new Array<UserGiftCard>();
  public recievedGiftCards: UserGiftCard[] = new Array<UserGiftCard>();
  public boughtedGiftCards: GiftCard[] = new Array<GiftCard>();

  constructor(private userService: UserService,private giftCardService: GiftcardService) {
  }

  ngOnInit(): void {
    this.userService
      .getUserByEmail()
      .subscribe((user: User) => {
        this.user = user;
      });

      this.giftCardService
      .getGiftCardsSendByUser()
      .subscribe((giftCards: UserGiftCard[]) => {
        this.sentGiftCards = giftCards;
     });
     
     this.giftCardService
      .getGiftCardsReciviedByUser()
      .subscribe((recievedGiftCards: UserGiftCard[]) => {
        this.recievedGiftCards = recievedGiftCards;
     });
     this.giftCardService
      .getBoughtedGiftCards()
      .subscribe((boughtedGiftCards: GiftCard[]) => {
        this.boughtedGiftCards = boughtedGiftCards;
     });
  }

}
