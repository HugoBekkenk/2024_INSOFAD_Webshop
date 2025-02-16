import { Component, OnInit } from '@angular/core';
import { CartService } from "../services/cart.service";
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from "@angular/forms";
import { Router } from "@angular/router";
import { Order } from '../models/order.model';
import { ProductCart } from '../models/product-cart.model';
import { User } from '../models/user.model';
import { OrderDTO } from '../models/order-dto.model';
import { UserService } from '../services/user.service';
import { IDropdownSettings, NgMultiSelectDropDownModule } from 'ng-multiselect-dropdown';
import { MiniGiftCard } from '../models/minigiftcard.model';
import { GiftcardService } from '../services/giftcard.service';
import { Product } from '../models/product.model';
import { ProductOrderDTO } from '../models/product-order-dto.model';

@Component({
  selector: 'app-order',
  templateUrl: './order.component.html',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    FormsModule,
    NgMultiSelectDropDownModule
  ],
  styleUrls: ['./order.component.scss']
})
export class OrderComponent implements OnInit {
  public bestelForm: FormGroup;
  public products_in_cart: ProductCart[];
  public productsOrder: ProductOrderDTO[] = [];
  public order: Order;
  public user: User;
  dropdownList: any[] = [];
  selectedItems: MiniGiftCard[] = [];
  public miniGiftCards: MiniGiftCard[] = new Array<MiniGiftCard>();
  dropdownSettings: IDropdownSettings;
  selectedIds: any[] = [];
  appliedPromoCode: string;
  public giftCardsInCart: ProductCart[] = []; 

  constructor(
    private cartService: CartService,
    private giftCardService: GiftcardService,
    private router: Router,
    private fb: FormBuilder,
    private userService: UserService
  ) {
    this.bestelForm = this.fb.group({
      name: ['', [Validators.required]],
      infix: [''],
      lastName: ['', [Validators.required]],
      zipCode: ['', [Validators.required]],
      houseNumber: ['', [Validators.required, Validators.maxLength(5)]],
      notes: ['']
    });
  }

  ngOnInit(): void {
    this.appliedPromoCode = localStorage.getItem('promoCode') || '';

    this.products_in_cart = this.cartService.allProductsInCart();
    this.products_in_cart.forEach(productCart => {
      if (productCart.type === 'GIFT_CARD') {
        this.giftCardsInCart.push(productCart);
      }

      this.dropdownSettings = {
        singleSelection: false,
        idField: 'id',
        textField: 'cardCode',
        selectAllText: 'Select All',
        unSelectAllText: 'UnSelect All',
        itemsShowLimit: 3,
        allowSearchFilter: true
      };
      this.giftCardService.getMiniGiftCards().subscribe((miniGiftCards: MiniGiftCard[]) => {
        this.miniGiftCards = miniGiftCards;
      });
    });

    this.userService.getUserByEmail().subscribe((user: User) => {
      this.user = user;
      this.bestelForm.patchValue({
        name: this.user.name,
        infix: this.user.infix,
        lastName: this.user.lastName
      });
    });
  }

  private transformProductCartToProductOrder(productCart: ProductCart): ProductOrderDTO {
    return {
      product_id: productCart.id,
      variantOrders: productCart.variants.map(variant => ({
        variant_id: variant.id,
        option_id: variant.option.id
      })),
      type: productCart.type 
    };
  }

  public clearCart() {
    this.cartService.clearCart();
  }

  public onSubmit() {
    const formData = this.bestelForm.value;

    this.productsOrder = this.products_in_cart.map(productCart => 
      this.transformProductCartToProductOrder(productCart)
    );

    // Prepare the giftCards array with full details from the cart
    const giftCardsFromCart: Product[] = this.giftCardsInCart.map(cartItem => ({
      id: cartItem.id,
      name: cartItem.name,
      description: 'Gift Card', 
      price: cartItem.price,
      imgURL: cartItem.imgURL,
      amount: 1, 
      specifications: '', 
      releaseDate: '',
      publisher: 'Gift Card Publisher', 
      type: 'GIFT_CARD', 
      variants: [], 
      stock: 50 
    }));

    const totalPrice = this.cartService.calculateTotalPrice();
    const discountedPrice = this.cartService.totalPriceWithDiscount;



  let orderDTO : OrderDTO;
    if(localStorage.getItem("bought")=="GiftCard"){
      orderDTO= new OrderDTO(
        formData.name,
        formData.infix,
        formData.lastName,
        formData.zipCode,
        formData.houseNumber,
        formData.notes,
        this.productsOrder,
        this.appliedPromoCode, 
        this.selectedIds,
        [],
      );
    }else{
        orderDTO= new OrderDTO(
        formData.name,
        formData.infix,
        formData.lastName,
        formData.zipCode,
        formData.houseNumber,
        formData.notes,
        this.productsOrder,
        this.appliedPromoCode, 
        this.selectedIds,
        [],
      );
    }

    console.log(orderDTO);
    this.cartService.addOrder(orderDTO).subscribe(
      (result) => {
        console.log('Order added successfully:', result);
        localStorage.setItem("remainingAmount", result.remainingAmount.toString());
        this.clearCart();
        this.router.navigateByUrl('/paymentsuccessful');
      },
      (error) => {
        console.error('Failed to add order:', error);
      }
    );
    localStorage.removeItem("bought");
  }

  onItemSelect(item: any) {
    console.log(item);
    this.selectedItems.push(item);
    this.selectedIds.push(item.id);
  }

  onSelectAll(items: any) {
    console.log(items);
    this.selectedIds.length = 0;
    this.selectedItems.length = 0;
    this.selectedItems.push(items);
    for (let item of items) {
      this.selectedIds.push(item.id);
    }
  }
}
