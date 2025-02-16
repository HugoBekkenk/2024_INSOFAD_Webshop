import { VariantOrderDTO } from "./variant-order-dto.model";

export class ProductOrderDTO {
  public product_id: number;
  public variantOrders: VariantOrderDTO[]
  public type: string;
  constructor(product_id: number, variantOrders: VariantOrderDTO[], type: string){
    this.product_id = product_id;
    this.variantOrders = variantOrders;
    this.type = type;
  }

 }
