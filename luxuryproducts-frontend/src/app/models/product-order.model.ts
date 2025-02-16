import { VariantOrder } from "./variant-order.model";

export class ProductOrder { 
  public id: number;
  public productId: number;
  public name: string;
  public description: string;
  public price: number;
  public imgURL: string;
  public amount: number;
  public specifications: string;
  public releaseDate: string;
  public publisher: string;
  public categoryName: string;
  public variants: VariantOrder[];
  public type: string; 

  constructor(
    id: number,
    productId: number,
    name: string,
    description: string,
    price: number,
    imgURL: string,
    amount: number,
    specifications: string,
    releaseDate: string,
    publisher: string,
    categoryName: string,
    variants: VariantOrder[],
    type: string
  ) {
    this.id = id;
    this.productId = productId;
    this.name = name;
    this.description = description;
    this.price = price;
    this.imgURL = imgURL;
    this.amount = amount;
    this.specifications = specifications;
    this.releaseDate = releaseDate;
    this.publisher = publisher;
    this.categoryName = categoryName;
    this.variants = variants;
    this.type = type; 
  }
}
