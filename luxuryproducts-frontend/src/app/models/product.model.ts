import { Variant } from "./variant.model";

export class Product {
  public id: number;
  public name: string;
  public description: string;
  public price: number;
  public imgURL: string;
  public amount: number;
  public specifications: string;
  public publisher: string;
  public releaseDate: string;
  public variants: Variant[];
  public stock: number;
  public type: string; 
  
  constructor(id: number, name: string, description: string, price: number, imgURL: string, amount: number, specifications: string, publisher: string, releaseDate: string, variants: Variant[], type: string) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.price = price;
    this.imgURL = imgURL;
    this.amount = amount;
    this.specifications = specifications;
    this.publisher = publisher;
    this.releaseDate = releaseDate;
    this.variants = variants;
    this.type = type;
  }
}
