import { VariantCart } from "./variant-cart.model";

export class ProductCart {
  public id: number;
  public name: string;
  public description: string;
  public price: number;
  public imgURL: string;
  public amount: number;
  public specifications: string;
  public publisher: string;
  public releaseDate: string;
  public variants: VariantCart[];
  public type: string;

  constructor(id: number, name: string, description: string, price: number, imgURL: string, amount: number, specifications: string, publisher: string, releaseDate: string, varient: VariantCart[], type: string){
    this.id = id;
    this.name = name;
    this.description = description;
    this.price = price;
    this.imgURL = imgURL;
    this.amount = amount;
    this.specifications = specifications;
    this.publisher = publisher;
    this.releaseDate = releaseDate;
    this.variants = varient;
    this.type = type;
  }
 }
