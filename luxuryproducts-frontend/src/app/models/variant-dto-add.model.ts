import { OptionDTO } from "./option-dto.model";

export class VariantDtoAdd {
  public name: string;
  public productId: number;
  public options: OptionDTO[];

  constructor(name: string, productId: number, options: OptionDTO[]){
    this.name = name;
    this.productId = productId;
    this.options = options;
  }
 }
