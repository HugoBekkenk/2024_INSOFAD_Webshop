import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ProductsService } from '../services/products.service';
import { AdminServiceService } from '../services/admin-service.service';
import { GiftcardService } from '../services/giftcard.service';
import { Product } from '../models/product.model';
import { ProductOrder } from '../models/product-order.model';
import { Variant } from '../models/variant.model';
import { Option } from '../models/option.model';
import { ProductDTO } from '../models/product-dto.model';
import { VariantDtoAdd } from '../models/variant-dto-add.model';
import { VariantDTO } from '../models/variant-dto.model';
import { OptionDtoAdd } from '../models/option-dto-add.model';
import { OrderRecieved } from '../models/order-recieved.model';
import { OptionDTO } from '../models/option-dto.model';
import { GiftCard } from '../models/giftcard.model';
import { UserGiftCard } from '../models/usergiftcard.model';
import { RouterModule } from '@angular/router';
import { TokenService } from '../auth/token.service';

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [CommonModule, RouterModule, ReactiveFormsModule, FormsModule],
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.scss']
})
export class AdminComponent implements OnInit {
  public showProducts: boolean = true;
  public showOrders: boolean = false;
  public showGiftcard: boolean = false;
  public products: Product[];
  public filteredProducts: Product[];
  public orders: OrderRecieved[];
  public loadingProducts = true;
  public loadingOrders = true;
  public ordersPresent = false;
  public addingProduct = false;
  public addingVariant = false;
  public addingOption = false;
  public filteringProducts = true;
  public filteringGiftcards = false;
  public productForm: FormGroup;
  public variantForm: FormGroup;
  public optionForm: FormGroup;
  public productToAdd: ProductDTO;
  public variantToAdd: VariantDtoAdd;
  public variantsToAdd: VariantDTO[] = [];
  public optionToAdd: OptionDtoAdd;

  // Gift Card Properties
  public bestelForm: FormGroup;
  public giftCard: GiftCard;
  public giftCards: GiftCard[] = [];
  public loadingGiftCards: boolean = true;
  public userGiftCards: UserGiftCard[] = [];

  constructor(
    private productsService: ProductsService, 
    private adminService: AdminServiceService, 
    private giftCardService: GiftcardService,
    private fb: FormBuilder,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.initForms();
    this.loadProducts();
    this.loadOrders();
    this.loadGiftCards();
    this.loadUserGiftCards();
  }

  initForms(): void {
    this.productForm = this.fb.group({
      name: ['', [Validators.required]],
      category: ['', [Validators.required]],
      price: ['', [Validators.required]],
      releaseDate: ['', [Validators.required]],
      stock: ['', [Validators.required]],
      imgURL: ['', [Validators.required]],
      publisher: ['', [Validators.required]],
      description: ['', [Validators.required]],
      specifications: ['', [Validators.required]],
      variant: ['', [Validators.required]],
      option: ['', [Validators.required]],
      priceAdded: ['', [Validators.required]]
    });
    this.optionForm = this.fb.group({
      name: ['', [Validators.required]],
      priceAdded: ['', [Validators.required]],
    });
    this.variantForm = this.fb.group({
      name: ['', [Validators.required]],
      option: ['', [Validators.required]],
      priceAdded: ['', [Validators.required]],
    });
    this.bestelForm = this.fb.group({
      name: ['', [Validators.required]],
      amount: ['', [Validators.required]],
      image: [''],
    });
  }

  public loadProducts(): void {
    this.productsService.getProducts().subscribe((products: Product[]) => {
      this.loadingProducts = false;
      this.products = products.sort((a, b) => b.id - a.id);
      this.products.forEach(product => {
        product.variants.sort((a, b) => a.id - b.id);
        product.variants.forEach(variant => {
          variant.options.sort((a, b) => a.priceAdded - b.priceAdded);
        });
      });
      this.filteredProducts = this.products;
      if (this.filteringProducts){
        this.onFilterProducts();
      } else if (this.filteringGiftcards){
        this.onFilterGiftcards();
      }
    });
  }

  public loadOrders(): void {
    this.adminService.getAllOrders().subscribe((orders: OrderRecieved[]) => {
      this.loadingOrders = false;
      this.orders = orders.sort((a, b) => b.id - a.id);
      this.ordersPresent = !!this.orders.length;
      this.orders.forEach(order => {
        order.products.forEach(product => {
          product.variants.sort((a, b) => a.id - b.id);
        });
      });
    });
  }

  public loadGiftCards(): void {
    this.giftCardService.getGiftCards().subscribe((giftCards: GiftCard[]) => {
      this.loadingGiftCards = false;
      this.giftCards = giftCards;
    });
  }

  public loadUserGiftCards(): void {
    this.giftCardService.getAllUsersGiftCards().subscribe((userGiftCards: UserGiftCard[]) => {
      this.userGiftCards = userGiftCards;
    });
  }

  public onClickOption(option: string): void {
    this.showProducts = false;
    this.showOrders = false;
    this.showGiftcard = false;
    this.addingProduct = false;
    if (option === "products") this.showProducts = true;
    if (option === "orders") this.showOrders = true;
    if (option === "giftcards") this.showGiftcard = true;
  }

  public onCLickAddProduct(): void {
    this.addingProduct = true;
  }

  public onRemoveProduct(product: Product): void {
    
    this.adminService.DeleteProductByIndex(product.id).subscribe((respone) => {
    });
    alert("removed product: " + product.name);
    this.loadProducts();
  }

  public onSubmitProductForm(): void {
    const formData = this.productForm.value;
    let options: OptionDTO[] = [new OptionDTO(formData.option, formData.priceAdded)];
    let variant: VariantDTO = new VariantDTO(formData.variant, options);
    this.variantsToAdd.push(variant);

    this.productToAdd = {
      name: formData.name,
      description: formData.description,
      price: formData.price,
      imgURL: formData.imgURL,
      specifications: formData.specifications,
      releaseDate: formData.releaseDate,
      publisher: formData.publisher,
      stock: formData.stock,
      category: formData.category,
      variants: this.variantsToAdd,
      type: "PRODUCT"
    };

    console.log(this.productToAdd);
    

    this.adminService.createProduct(this.productToAdd).subscribe(() => {
    });
    this.addingProduct = false;
    alert("Added product!");
    window.location.reload();
  }

  calculateProductPrice(product: ProductOrder): number {
    let price = product.price;
    product.variants.forEach(variant => {
      price += variant.priceAdded;
    });
    return price;
  }

  public onCLickRemoveVariant(id: number, variants: Variant[]): void {
    if (variants.length <= 1) {
      alert("Product must have at least one variant");
    } else {
      this.adminService.deleteVariant(id).subscribe(() => {
      });
      alert("removed variant");
      this.loadProducts();
    }
  }

  public onCLickRemoveOption(id: number, options: Option[]): void {
    if (options.length <= 1) {
      alert("Variant must have at least one option");
    } else {
      this.adminService.deleteOption(id).subscribe(() => {
      });
      alert("removed option");
      this.loadProducts();
    }
  }

  public onClickAddOption(): void {
    this.addingOption = true;
  }

  public onClickAddVariant(): void {
    this.addingVariant = true;
  }

  public onSubmitOptionForm(variantId: number): void {
    const formData = this.optionForm.value;
    this.optionToAdd = {
      name: formData.name,
      priceAdded: formData.priceAdded,
      variantId: variantId
    };

    this.adminService.createOption(this.optionToAdd).subscribe(() => {
    });
    this.addingOption = false;
    alert("Added option!");
    this.loadProducts();
    this.optionForm.reset();
  }

  public onSubmitVariantForm(productId: number): void {
    const formData = this.variantForm.value;
    let options: OptionDTO[] = [new OptionDTO(formData.option, formData.priceAdded)];

    this.variantToAdd = {
      name: formData.name,
      productId: productId,
      options: options
    };

    this.adminService.createVariant(this.variantToAdd).subscribe(() => {
    });
    this.addingVariant = false;
    alert("Added variant!");
    this.loadProducts();
    this.variantForm.reset();
  }

  // Gift Card Methods
  public onSubmitGiftCardForm(): void {
    const formData = this.bestelForm.value;
    this.giftCard = {
      id: formData.id,
      name: formData.name,
      amount: formData.amount,
      image: formData.image,
      cardCode: ''
    };

    this.giftCardService.createGiftCard(this.giftCard).subscribe(
      (result) => {
        console.log('Gift Card added successfully:', result);
        alert(result.message);
        this.router.navigateByUrl('/admin-gift-cards');
        window.location.reload();
      },
      (error) => {
        console.error('Failed to add gift card:', error);
        alert('Failed to add Gift Card');
        window.location.reload();
      }
    );
  }

  public deleteCard(id: number): void {
    this.giftCardService.deleteGiftCardById(id).subscribe(
      (result) => {
        console.log('Gift Card deleted successfully:', result);
        alert(result.message);
        window.location.reload();
      },
      (error) => {
        console.error('Failed to delete gift card:', error);
        alert('Failed to delete Gift Card');
        window.location.reload();
      }
    );
  }

  public onFilterProducts(): void{
    let newProducts: Product[] = [];
    this.filteringGiftcards = false;
    this.filteringProducts = true;
    this.products.forEach(product =>{
      if (product.type === "PRODUCT"){
        newProducts.push(product);
      }
    })
    this.filteredProducts = newProducts;
  }

  public onFilterGiftcards(): void{
    let newProducts: Product[] = [];
    this.filteringGiftcards = true;
    this.filteringProducts = false;
    this.products.forEach(product =>{
      if (product.type === "GIFT_CARD"){
        newProducts.push(product);
      }
    })
    this.filteredProducts = newProducts;
  }
}
