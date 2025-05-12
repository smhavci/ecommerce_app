import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { UserRoutingModule } from './user-routing.module';

import { ProductListComponent } from './product-list/product-list.component';
import { ProductDetailComponent } from './product-detail/product-detail.component';
import { CartComponent } from './cart/cart.component';
import { CheckoutComponent } from './checkout/checkout.component';
import { OrderHistoryComponent } from './order-history/order-history.component';
import { RouterModule } from '@angular/router';
import { UserProfileComponent } from './user-profile/user-profile.component';

@NgModule({
  declarations: [
    ProductListComponent,
    ProductDetailComponent,
    CartComponent,
    CheckoutComponent,
    OrderHistoryComponent,
    UserProfileComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    UserRoutingModule,
    RouterModule
  ]
})
export class UserModule {}
