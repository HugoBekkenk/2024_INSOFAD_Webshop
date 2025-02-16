import { GiftCard } from "./giftcard.model";
import { ProductOrderDTO } from "./product-order-dto.model";

export class OrderDTO {
  public name: string;
  public infix: string;
  public last_name: string;
  public zipcode: string;
  public houseNumber: number;
  public notes: string;
  public product_ids: ProductOrderDTO[];
  public promoCode: string;
  public giftCardIds: number[];
  public giftCards: GiftCard[]; 

  constructor(name: string, infix: string, lastname: string, zipcode: string, housenumber: number, notes: string, products: ProductOrderDTO[], promoCode: string, giftCardIds: number[], giftCards: GiftCard[]) {
    this.name = name;
    this.infix = infix;
    this.last_name = lastname;
    this.zipcode = zipcode;
    this.houseNumber = housenumber;
    this.notes = notes;
    this.product_ids = products;
    this.promoCode = promoCode;
    this.giftCardIds = giftCardIds;
    this.giftCards = giftCards; 
  }
}
