import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { GiftCard } from '../models/giftcard.model';
import { UserGiftCard } from '../models/usergiftcard.model';

import {Observable} from 'rxjs';

import {environment} from '../../environments/environment';
import { MiniGiftCard } from '../models/minigiftcard.model';

@Injectable({
  providedIn: 'root'
})
export class GiftcardService {
  private baseUrl: string = environment.base_url + "/giftCards";
  private orderBaseURL: string = environment.base_url + "/orders";

  constructor(private http: HttpClient) {
  }
  public getGiftCards(): Observable<GiftCard[]> {
    return this.http.get<GiftCard[]>(this.baseUrl);
  }

  public createGiftCard(giftCardData: any): Observable<any> {
    return this.http.post<any>(this.baseUrl, giftCardData);
  }

  public getGiftCardtById(id: number): Observable<GiftCard> {
    return this.http.get<GiftCard>(`${this.baseUrl}/${id}`);
  }

  public deleteGiftCardById(id: number): Observable<any> {
    return this.http.delete<any>(`${this.baseUrl}/${id}`);
  }

  public updateGiftCardById(id: number, giftcard: GiftCard): Observable<GiftCard> {
    return this.http.put<GiftCard>(`${this.baseUrl}/${id}`, giftcard);
  }
  public sendGiftCard(id: number,email: string): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/${email}/${id}`,null);
  }
  public getMiniGiftCards(): Observable<MiniGiftCard[]> {
    return this.http.get<MiniGiftCard[]>(this.baseUrl+'/mini');
  }
  public getGiftCardsSendByUser(): Observable<UserGiftCard[]> {
    return this.http.get<UserGiftCard[]>(this.baseUrl+'/getAllGiftCardSentByUser');
  }
  public getGiftCardsReciviedByUser(): Observable<UserGiftCard[]> {
    return this.http.get<UserGiftCard[]>(this.baseUrl+'/getAllGiftCardsReceivedByUser');
  }
  public getAllUsersGiftCards(): Observable<UserGiftCard[]> {
    return this.http.get<UserGiftCard[]>(this.baseUrl+'/users');
  }
  public getBoughtedGiftCards(): Observable<GiftCard[]> {
    return this.http.get<GiftCard[]>(this.orderBaseURL+'/myGiftCards');
  }
}
