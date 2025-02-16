import { ProductOrder } from "./product-order.model";
import { Product } from "./product.model"; 

export class Order {
  public id: number;
  public name: string;
  public infix: string;
  public last_name: string;
  public zipcode: string;
  public houseNumber: number;
  public notes: string;
  public orderDate: string;
  public products: ProductOrder[];
  public totalPrice: number;
  public discountedPrice: number;
  public promoCode: string;
  public giftCardIds: number[];
  public giftCards: Product[]; 
  public paidAmountByGiftCard: number;

  constructor(id: number, name: string, infix: string, last_name: string, zipcode: string, houseNumber: number, notes: string, orderDate: string, products: ProductOrder[], totalPrice: number, discountedPrice: number, promoCode: string, giftCardIds: number[], giftCards: Product[], paidAmountByGiftCard: number) {
    this.id = id;
    this.name = name;
    this.infix = infix;
    this.last_name = last_name;
    this.zipcode = zipcode;
    this.houseNumber = houseNumber;
    this.notes = notes;
    this.orderDate = orderDate;
    this.products = products;
    this.totalPrice = totalPrice;
    this.discountedPrice = discountedPrice;
    this.promoCode = promoCode;
    this.giftCardIds = giftCardIds;
    this.giftCards = giftCards; 
    this.paidAmountByGiftCard = paidAmountByGiftCard;
  }
}
