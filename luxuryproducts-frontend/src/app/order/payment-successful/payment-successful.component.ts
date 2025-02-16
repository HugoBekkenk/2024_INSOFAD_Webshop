import { Component, OnInit } from '@angular/core';
import {Router, RouterLink} from "@angular/router";

@Component({
  selector: 'app-payment-successful',
  standalone: true,
  imports: [
    RouterLink
  ],
  templateUrl: './payment-successful.component.html',
  styleUrl: './payment-successful.component.scss'
})
export class PaymentSuccessfulComponent implements OnInit{
  
  public remainingAmount : number = 0;
  
  ngOnInit(): void {
    
    let value = localStorage.getItem("remainingAmount");
    localStorage.setItem('promoCode', '');
    localStorage.setItem('displayedDiscount', '');
    if(value!=null){
      this.remainingAmount =  parseFloat(value);
      this.remainingAmount.toPrecision(2);
    }
    
  }

}
