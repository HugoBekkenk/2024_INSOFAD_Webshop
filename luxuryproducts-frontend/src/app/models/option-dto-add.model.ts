export class OptionDtoAdd {
  public name: string;
  public priceAdded: number;
  public variantId: number

  constructor(name: string, priceAdded: number, variantId: number){
    this.name = name;
    this.priceAdded = priceAdded;
    this.variantId = variantId;
  }
 }
