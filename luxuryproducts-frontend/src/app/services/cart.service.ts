import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, catchError, throwError } from 'rxjs';
import { ProductCart } from '../models/product-cart.model';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { OrderDTO } from '../models/order-dto.model';
import { OrderReponse } from '../models/orderresponse.model';

const localStorageKey: string = "products-in-cart";
const promoAppliedKey: string = "promoApplied";

@Injectable({
  providedIn: 'root'
})
export class CartService {
  private productsInCart: ProductCart[] = [];
  public $productInCart: BehaviorSubject<ProductCart[]> = new BehaviorSubject<ProductCart[]>([]);
  public totalDiscount: number = 0;
  public totalPriceWithDiscount: number = this.loadInitialDiscountedPrice();
  private baseUrl: string = environment.base_url + "/orders";

  constructor(private http: HttpClient) {
    this.loadProductsFromLocalStorage();
    this.reapplyDiscountIfApplicable();
  }

  private reapplyDiscountIfApplicable() {
    const discountValue = parseFloat(localStorage.getItem('discountValue') || '0');
    const discountType = localStorage.getItem('discountType') as 'FIXED_AMOUNT' | 'PERCENTAGE' | null;
    const promoCode = localStorage.getItem('promoCode') || '';  // Retrieve stored promo code

    if (discountType && discountValue && promoCode) {
      this.applyDiscount(discountValue, discountType, promoCode);
    }
  }

  public addProductToCart(productToAdd: ProductCart) {
    this.productsInCart.push(productToAdd);
    this.saveProductsAndNotifyChange();
  }

  public removeProductFromCart(productIndex: number) {
    if (this.productsInCart[productIndex].amount > 1) {
      this.productsInCart[productIndex].amount -= 1;
    } else {
      this.productsInCart.splice(productIndex, 1);
    }
    this.saveProductsAndNotifyChange();
    localStorage.removeItem(promoAppliedKey);
  }

  public clearCart() {
    this.productsInCart = [];
    localStorage.removeItem(promoAppliedKey);  // Reset promo code status on cart clear
    this.saveProductsAndNotifyChange();
  }

  public allProductsInCart(): ProductCart[] {
    return this.productsInCart.slice();
  }

  public addOrder(order: OrderDTO): Observable<OrderReponse> {
    console.log("Received order: " + order);
    return this.http.post<OrderReponse>(this.baseUrl, order).pipe(
      catchError(error => {
        console.error('Error adding order:', error);
        return throwError(error);
      })
    );
  }

  public applyDiscount(discountValue: number, discountType: 'FIXED_AMOUNT' | 'PERCENTAGE', promoCode: string) {
    const totalProductPrice = this.calculateTotalProductPrice(); // Only products' prices

    if (discountType === 'FIXED_AMOUNT') {
      this.totalDiscount = discountValue;
    } else if (discountType === 'PERCENTAGE') {
      this.totalDiscount = totalProductPrice * (discountValue / 100);
    }

    const total = this.calculateTotalPrice(); // Total price including gift cards
    this.totalPriceWithDiscount = total - this.totalDiscount;

    localStorage.setItem('promoApplied', 'true');
    localStorage.setItem('promoCode', promoCode);
    localStorage.setItem('discountValue', discountValue.toString());
    localStorage.setItem('discountType', discountType);
    localStorage.setItem('displayedDiscount', this.totalDiscount.toString());
    this.$productInCart.next(this.productsInCart.slice());
  }

  public removeDiscount() {
    this.totalDiscount = 0;
    this.totalPriceWithDiscount = this.calculateTotalPrice();
    localStorage.removeItem('promoApplied');
    localStorage.removeItem('promoCode'); 
    localStorage.removeItem('discountValue');
    localStorage.removeItem('discountType');
    this.$productInCart.next(this.productsInCart.slice());
  }

  public calculateTotalProductPrice(): number {
    let totalProductPrice = 0;
    this.productsInCart.forEach(product => {
      if (product.type !== 'GIFT_CARD') { 
        totalProductPrice += product.price;
        product.variants.forEach(variant => {
          totalProductPrice += variant.option.priceAdded;
        });
      }
    });
    return totalProductPrice;
  }

  public calculateTotalPrice(): number {
    let total = 0;
    this.productsInCart.forEach(product => {
      total += product.price;
      product.variants.forEach(variant => {
        total += variant.option.priceAdded;
      });
    });
    return total;
  }

  private loadInitialDiscountedPrice(): number {
    const total = this.calculateTotalPrice();
    const discountValue = parseFloat(localStorage.getItem('discountValue') || '0');
    const discountType = localStorage.getItem('discountType') as 'FIXED_AMOUNT' | 'PERCENTAGE' | null;

    if (discountType === 'FIXED_AMOUNT') {
      return Math.max(0, total - discountValue);
    } else if (discountType === 'PERCENTAGE') {
      return Math.max(0, total - (total * discountValue / 100));
    }
    return total;
  }

  // ------------ PRIVATE ------------------
  private saveProductsAndNotifyChange(): void {
    this.saveProductsToLocalStorage(this.productsInCart.slice());
    this.$productInCart.next(this.productsInCart.slice());
  }

  private saveProductsToLocalStorage(products: ProductCart[]): void {
    localStorage.setItem(localStorageKey, JSON.stringify(products));
  }

  private loadProductsFromLocalStorage(): void {
    let productsOrNull = localStorage.getItem(localStorageKey);
    if (productsOrNull != null) {
      let products: ProductCart[] = JSON.parse(productsOrNull);
      this.productsInCart = products;
      this.$productInCart.next(this.productsInCart.slice());
    }
  }
}
