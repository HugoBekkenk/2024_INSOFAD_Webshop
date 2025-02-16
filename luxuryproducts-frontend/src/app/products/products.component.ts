import {Component} from '@angular/core';

import {CartService} from '../services/cart.service';
import {Product} from '../models/product.model';
import {ProductsService} from '../services/products.service';
import { ProductCart } from '../models/product-cart.model';

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrl: './products.component.scss'
})
export class ProductsComponent {
  public products: Product[] = new Array<Product>();
  public filteredProducts: Product[];
  public loadingProducts: boolean = true;

  constructor(private productsService: ProductsService, private cartService: CartService) {
  }

  ngOnInit(): void {
    this.productsService
      .getProducts()
      .subscribe((products: Product[]) => {
        this.loadingProducts = false;
        this.products = products;
        this.filteredProducts = products;
      });
  }

  public onBuyProduct(product: ProductCart) {
    this.cartService.addProductToCart(product)
  }

  public onFilterProducts(): void{
    let newProducts: Product[] = [];
    this.products.forEach(product =>{
      if (product.type === "PRODUCT"){
        newProducts.push(product);
      }
    })
    this.filteredProducts = newProducts;
  }

  public onFilterGiftcards(): void{
    let newProducts: Product[] = [];
    this.products.forEach(product =>{
      if (product.type === "GIFT_CARD"){
        newProducts.push(product);
      }
    })
    this.filteredProducts = newProducts;
  }
}
