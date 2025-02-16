import { GiftCard } from "./giftcard.model";
import { User } from "./user.model";

export class UserGiftCard {
  public id: number;
  public sendBy: User;
  public receivedBy: User;
  public giftCard: GiftCard;
}
