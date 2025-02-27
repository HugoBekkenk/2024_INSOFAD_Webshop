import {Component, OnInit} from '@angular/core';
import { AuthService } from '../../auth/auth.service';
import { Router } from '@angular/router';
import { CartService } from '../../services/cart.service';
import { Product } from '../../models/product.model';
import { ProductCart } from '../../models/product-cart.model';

 
@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent implements OnInit {
 
  public userIsLoggedIn: boolean = false;
  public userIsAdmin: boolean = false;
  public isDropdownOpen: boolean = false;
  public amountOfProducts: number = 0;
 
  constructor(private authService: AuthService, private router: Router, private cartService: CartService) {
  }
 
  public ngOnInit(): void {
    this.checkLoginState();
    this.cartService.$productInCart.subscribe((products: ProductCart[]) => {
      this.amountOfProducts = products.reduce((total, product) => total + product.amount, 0);
    });
  }
 
  public onLogout(): void {
    this.authService.logOut();
    this.router.navigate(['/']);
  }
 
  public checkLoginState(): void {
 
    this.authService
      .$userIsLoggedIn
      .subscribe((loginState: boolean) => {
        this.userIsLoggedIn = loginState;
      });

    this.authService
      .$userIsAdmin
      .subscribe((adminState: boolean) => {
        this.userIsAdmin = adminState;
      })
  }
 
  toggleDropdown() {
    this.isDropdownOpen = !this.isDropdownOpen;
  }
 
}
 