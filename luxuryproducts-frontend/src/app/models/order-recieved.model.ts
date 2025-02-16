import { ProductOrder } from "./product-order.model";
import { User } from "./user.model";

export class OrderRecieved { 
  public id: number;
  public name: string;
  public infix: string;
  public last_name: string;
  public zipcode: string;
  public houseNumber: number;
  public notes: string;
  public orderDate: string;
  public totalPrice: number;
  public discountedPrice: number;
  public giftCardIds:number[];
  public paidAmountByGiftCard:number;
  public promoCode: string;
  public user: User;
  public products: ProductOrder[];
}
