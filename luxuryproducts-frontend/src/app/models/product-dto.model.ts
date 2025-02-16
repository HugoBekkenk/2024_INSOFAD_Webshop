import { VariantDTO } from "./variant-dto.model";

export class ProductDTO {
  public name: string;
  public description: string;
  public price: number;
  public imgURL: string;
  public specifications: string;
  public releaseDate: string;
  public publisher: string;
  public stock: number;
  public category: string;
  public type: string;
  public variants: VariantDTO[];
 }
