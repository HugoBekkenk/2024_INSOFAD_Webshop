export class VariantOrderDTO { 
  public variant_id: number;
  public option_id: number;

  constructor(variant_id: number, option_id: number){
    this.variant_id = variant_id;
    this.option_id = option_id;
  }

}
