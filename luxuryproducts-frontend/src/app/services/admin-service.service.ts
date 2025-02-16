import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Product } from '../models/product.model';
import { User } from '../models/user.model';
import { OrderRecieved } from '../models/order-recieved.model';
import { VariantDTO } from '../models/variant-dto.model';
import { OptionDTO } from '../models/option-dto.model';

@Injectable({
  providedIn: 'root'
})
export class AdminServiceService {
  private baseUrl: string = environment.base_url + "/admin";

  constructor(private http: HttpClient) {
  }

  createProduct(productData: any): Observable<any> {
      return this.http.post<any>(`${this.baseUrl}/product`, productData);
    }

  public DeleteProductByIndex(id: number): Observable<any> {
    return this.http.delete<any>(`${this.baseUrl}/product/${id}`);
  }

  public updateProductByIndex(id: Product, product: number): Observable<Product> {
    return this.http.put<Product>(`${this.baseUrl}/product/${id}`, product);
  }

  public updateUser(user: User): Observable<User> {
    return this.http.put<User>(`${this.baseUrl}/user`, user);
  }

  public deleteUser(email: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/user/${email}`);
  }

  public getAllOrders(): Observable<OrderRecieved[]> {
    return this.http.get<OrderRecieved[]>(`${this.baseUrl}/orders`);
  }

  createVariant(variant: VariantDTO): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/variant`, variant);
  }

  createOption(option: OptionDTO): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/option`, option);
  }

  public deleteVariant(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/variant/${id}`);
  }

  public deleteOption(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/option/${id}`);
  }
}
