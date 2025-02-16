import { CommonModule } from "@angular/common";
import { Order } from "../../models/order.model";
import { ProductOrder } from "../../models/product-order.model";
import { OrderService } from "../../services/order.service";
import { Component, OnInit } from "@angular/core";

@Component({
  selector: 'app-order-history',
  templateUrl: './order-history.component.html',
  imports: [CommonModule],
  standalone: true,
  styleUrls: ['./order-history.component.scss']
})
export class OrderHistoryComponent implements OnInit {
  orders: Order[];
  public loadingOrders: boolean = true;

  constructor(private orderService: OrderService) { }

  ngOnInit() {
    this.loadOrdersIn();
  }

  loadOrdersIn() {
    this.orderService.getOrdersByCurrentUser().subscribe(orders => {
      this.orders = orders;
      this.loadingOrders = false;
      this.orders.sort((a, b) => {
        if (a.id > b.id) {
          return -1;
        }
        if (a.id < b.id) {
          return 1;
        }
        return 0;
      });
      this.orders.forEach(order => {
        order.products.forEach(product => {
          product.variants.sort((a, b) => {
            if (a.id < b.id) {
              return -1;
            }
            if (a.id > b.id) {
              return 1;
            }
            return 0;
          });
        });
      });
    });
  }

  calculateProductPrice(product: ProductOrder): number {
    let price = product.price;
    product.variants.forEach(variant => {
      price += variant.priceAdded;
    });
    return price;
  }

  calculateTotal(products: ProductOrder[]): number {
    let total = 0;
    products.forEach(product => {
      total += product.price;
      product.variants.forEach(variant => {
        total += variant.priceAdded;
      });
    });
    return total;
  }
}
