import { Component, OnInit } from '@angular/core';
import { CurrencyPipe, NgFor, NgIf } from '@angular/common';
import { CartService } from '../services/cart.service';
import { ProductCart } from '../models/product-cart.model';
import { Router } from '@angular/router';
import { AuthService } from '../auth/auth.service';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { PromoCode } from "../models/promocode.model";
import { PromoCodeService } from '../services/promocode.service';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [CurrencyPipe, NgFor, NgIf, FormsModule],
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.scss']
})
export class CartComponent implements OnInit {
  
  public products_in_cart: ProductCart[];
  public userIsLoggedIn: boolean = false;
  public amountOfProducts: number = 0;
  public promoCode: string = '';
  public promoType: 'FIXED_AMOUNT' | 'PERCENTAGE' = 'FIXED_AMOUNT';
  discount: number = 0;
  promoApplied: boolean = this.checkPromoApplied();
  appliedPromoCode: string = localStorage.getItem('promoCode') || '';
  displayedDiscount: string = localStorage.getItem('displayedDiscount') || '0';
  availablePromoCodes: PromoCode[] = [];

  constructor(private cartService: CartService, private router: Router, private authService: AuthService, private http: HttpClient, private promoCodeService: PromoCodeService) {}

  ngOnInit() {
    this.products_in_cart = this.cartService.allProductsInCart();
    this.cartService.$productInCart.subscribe((products: ProductCart[]) => {
      this.products_in_cart = products;
      this.amountOfProducts = products.length;
      this.checkLoginState();
      if (this.promoApplied) {
        this.discount = parseFloat(this.displayedDiscount);  // Use the stored discount if promo is applied
      }
    });
    this.fetchPromoCodes();
  }

  public clearCart() {
    this.cartService.clearCart();
    this.promoApplied = false;
    localStorage.removeItem("bought");
  }

  public removeProductFromCart(product_index: number) {
    this.cartService.removeProductFromCart(product_index);
  }

  public getTotalPrice(): number {
    return this.cartService.calculateTotalPrice();
  }

  public getTotalPriceWithDiscount(): number {
    return this.cartService.totalPriceWithDiscount;
  }

  onInvalidOrder() {
    return this.amountOfProducts === 0;
  }

  onOrder() {
    if (!this.userIsLoggedIn) {
      this.router.navigateByUrl('/auth/login');
    } else {
      this.router.navigateByUrl('/orders');
    }
  }

  public checkLoginState(): void {
    this.authService.$userIsLoggedIn.subscribe((loginState: boolean) => {
      this.userIsLoggedIn = loginState;
    });
  }

  applyPromoCode() {
    if (this.promoApplied) {
      alert('You can only use one promo code per order.');
      return;
    }

    if (this.products_in_cart.length === 0) {
      alert('No products found');
      return;
    }

    const promoCode = this.availablePromoCodes.find(promo => promo.code === this.promoCode);

    if (!promoCode) {
      alert('Invalid promo code');
      return;
    }

    // Check specific product variants and special promotions
    if (!this.validatePromoCodeForProductVariant(promoCode.code)) {
      return;
    }

    if (promoCode.code === 'PROMO1PLUS1') {
      this.applyPromo1Plus1();
    } else {
      this.discount = promoCode.discount;
      this.promoType = promoCode.type;
      this.cartService.applyDiscount(this.discount, this.promoType, this.promoCode);
    }

    this.promoApplied = true;
    this.appliedPromoCode = this.promoCode;
    localStorage.setItem('promoApplied', 'true');
    localStorage.setItem('promoCode', this.promoCode);
    localStorage.setItem('displayedDiscount', this.discount.toString());
  }

  validatePromoCodeForProductVariant(code: string): boolean {
    if (code === 'PROMOXBOX') {
      let isValid = false;
      for (const product of this.products_in_cart) {
        for (const variant of product.variants) {
          if (variant.name === 'Platform' && variant.option.name === 'Xbox') {
            isValid = true;
            break;
          }
        }
        if (isValid) break;
      }
      if (!isValid) {
        alert('Promo code is only for Xbox platform products');
        return false;
      }
    }

    if (code === 'PROMOPCDEFAULT') {
      let isValid = false;
      for (const product of this.products_in_cart) {
        let platformValid = false;
        let editionValid = false;
        for (const variant of product.variants) {
          if (variant.name === 'Platform' && variant.option.name === 'Pc') {
            platformValid = true;
          }
          if (variant.name === 'Edition' && variant.option.name === 'Default') {
            editionValid = true;
          }
        }
        if (platformValid && editionValid) {
          isValid = true;
          break;
        }
      }
      if (!isValid) {
        alert('Promo code is only for PC platform products with Default edition');
        return false;
      }
    }

    return true;
  }

  applyPromo1Plus1() {
    const mortalKombatId = 2; // Replace with the actual product ID for Mortal Kombat 1
    const rainbowSixSiegeId = 1; // Replace with the actual product ID for Tom Clancy's Rainbow Six Siege

    const eligibleProducts = this.products_in_cart.filter(product =>
      product.id === mortalKombatId || product.id === rainbowSixSiegeId
    );

    if (eligibleProducts.length < 2) {
      alert('Promo code is only valid if you have both eligible products in the cart');
      return;
    }

    // Find the cheaper product and make it free
    const freeProduct = eligibleProducts.find(product => product.id === rainbowSixSiegeId);
    if (freeProduct) {
      this.cartService.applyDiscount(freeProduct.price, 'FIXED_AMOUNT', this.promoCode);
      alert('1+1 promotion applied: Tom Clancy\'s Rainbow Six Siege is free!');
    } else {
      alert('Promo code is not applicable');
    }
  }

  removePromoCode() {
    this.cartService.removeDiscount();
    this.promoApplied = false;
    this.discount = 0;
    this.appliedPromoCode = ''; // Reset the displayed promo code
    alert('Promo code removed successfully.');
    localStorage.removeItem('promoApplied');
    localStorage.removeItem('promoCode');
    localStorage.removeItem('displayedDiscount');
  }

  private checkPromoApplied(): boolean {
    return localStorage.getItem('promoApplied') === 'true';
  }

  fetchPromoCodes() {
    this.promoCodeService.getAllPromoCodes().subscribe((promocodes: PromoCode[]) => {
      this.availablePromoCodes = promocodes;
    });
  }

  calculateProductPrice(product: ProductCart): number {
    let price = product.price;
    product.variants.forEach(variant => {
      price += variant.option.priceAdded;
    });
    return price;
  }

  calculateTotal(products: ProductCart[]): number {
    let total = 0;
    products.forEach(product => {
      total += product.price;
      product.variants.forEach(variant => {
        total += variant.option.priceAdded;
      });
    });
    return total;
  }

  
}
