import { Option } from "./option.model";

export class VariantCart {
  public id: number;
  public name: string;
  public option: Option;

  constructor(id: number, name:string, option: Option){
    this.id = id;
    this.name = name;
    this.option = option
  }
 }
