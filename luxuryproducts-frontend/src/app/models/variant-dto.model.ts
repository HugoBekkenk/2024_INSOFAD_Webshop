import { OptionDTO } from "./option-dto.model";

export class VariantDTO {
  public name: string;
  public options: OptionDTO[];

  constructor(name: string, options: OptionDTO[]){
    this.name = name;
    this.options = options;
  }
 }
