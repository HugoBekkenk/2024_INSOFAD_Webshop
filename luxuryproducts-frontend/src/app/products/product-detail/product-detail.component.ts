import {Component, Input} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {Product} from '../../models/product.model';
import {ProductsService} from '../../services/products.service';
import {CartService} from '../../services/cart.service';
import { Variant } from '../../models/variant.model';
import { Option } from '../../models/option.model';
import { VariantCart } from '../../models/variant-cart.model';
import { ProductCart } from '../../models/product-cart.model';

@Component({
  selector: 'app-product-detail',
  standalone: false,
  templateUrl: './product-detail.component.html',
  styleUrl: './product-detail.component.scss'
})
export class ProductDetailComponent {
  @Input() public product!: Product;
  public variants: Variant[];
  private productId: number;
  private chosenProduct: ProductCart;
  public currentPrice: number;
  public loadingProduct: boolean = true;
  public productAvailable: boolean = true;

  constructor(
    private activatedRoute: ActivatedRoute,
    private productsService: ProductsService,
    private cartService: CartService
  ) {
  }

  ngOnInit() {
    this.activatedRoute.params.subscribe(params => {
      this.productId = params['id'];
    });
  
    this.productsService
      .getProductByIndex(this.productId)
      .subscribe((product: Product) => {
        this.product = product;
        this.variants = product.variants;
        this.loadingProduct = false;
        if (product.stock <= 0) {
          this.productAvailable = false;
        }
        if (product.type === 'GIFT_CARD') {
          this.currentPrice = product.price;
        } else {
          this.variants.sort((a, b) => {
            if (a.id < b.id) {
              return -1;
            }
            if (a.id > b.id) {
              return 1;
            }
            return 0;
          });
          this.variants.forEach(variant => {
            variant.options.sort((a, b) => {
              if (a.priceAdded < b.priceAdded) {
                return -1;
              }
              if (a.priceAdded > b.priceAdded) {
                return 1;
              }
              return 0;
            });
          });
          this.currentPrice = product.price;
        }
      });
  }


  public onBuyProduct() {
    if (this.product.type === 'GIFT_CARD') {
      alert("Gift added to cart");
      this.cartService.addProductToCart(this.createProductCart());
    } else {
      if (this.chosenProduct !== undefined) {
        if (this.chosenProduct.variants.length === this.product.variants.length) {
          alert("Product added to cart");
          this.cartService.addProductToCart(this.chosenProduct);
        } else {
          alert("Click all the options");
        }
      } else {
        alert("Click all the options");
      }
    }
  }

  private createProductCart(): ProductCart {
    return new ProductCart(
      this.product.id,
      this.product.name,
      this.product.description,
      this.product.price,
      this.product.imgURL,
      1,
      this.product.specifications,
      this.product.publisher,
      this.product.releaseDate,
      [],
      'GIFT_CARD'
    );
  }
  

  public onChangeVariant(chosenOptionVariant: Variant, chosenOption: Option) {
    let chosenVariant: VariantCart | undefined;
    this.product.variants.forEach( variant => {
      if (chosenOptionVariant === variant){
          variant.options.forEach( option => {
            if (option === chosenOption){
              chosenVariant = new VariantCart(variant.id, variant.name, option);
              option.optionsSelected = true;
            } else{
              option.optionsSelected = false;
            }
        })
      }
    })
    let chosenVariants: VariantCart[] = [];
    if (this.chosenProduct === undefined){
      if (chosenVariant !== undefined){
        chosenVariants.push(chosenVariant);
      }
    } else{
      this.chosenProduct.variants.forEach( variant => {
        if (chosenVariant !== undefined){
        if (variant.id !== chosenVariant.id) {
          chosenVariants.push(variant)      
        } }
      })
      if (chosenVariant !== undefined){
        chosenVariants.push(chosenVariant)
      }
    }
    this.chosenProduct = new ProductCart(this.product.id, this.product.name, this.product.description, this.product.price, this.product.imgURL, 1, this.product.specifications, this.product.publisher, this.product.releaseDate, chosenVariants, 'PRODUCT');
    this.currentPrice = this.chosenProduct.price;
    this.chosenProduct.variants.forEach( variant => {
      this.currentPrice += variant.option.priceAdded;
    })
  }

}
