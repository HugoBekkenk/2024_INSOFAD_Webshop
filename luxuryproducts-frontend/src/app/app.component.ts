import {Component} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterOutlet} from '@angular/router';
import {AuthModule} from './auth/auth.module';
import {CoreModule} from './core/core.module';
import {ProductsModule} from "./products/products.module";
import { NgMultiSelectDropDownModule } from 'ng-multiselect-dropdown';
import { GiftCardsModule } from './gift-cards/gift-cards.module';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { GiftCardPopupModule } from './gift-cards/gift-card-popup/gift-card-popup.module';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, NgMultiSelectDropDownModule, RouterOutlet, AuthModule, CoreModule, ProductsModule, GiftCardsModule, NgbModule, GiftCardPopupModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  title = 'Gaming Webshop';
}
