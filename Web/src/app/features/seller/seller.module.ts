import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { SellerRoutingModule } from './seller-routing.module';
import { SellerComponent } from './seller.component';
import { ProductCreateComponent } from './product-create/product-create.component';
import { ReactiveFormsModule } from '@angular/forms';
import { SellerProfileComponent } from './seller-profile/seller-profile.component';
import { FormsModule } from '@angular/forms';
import { SellerOrdersComponent } from './seller-orders/seller-orders.component';
import { ProductManageComponent } from './product-manage/product-manage.component';
@NgModule({
  declarations: [
    SellerComponent,
    ProductCreateComponent,
    SellerProfileComponent,
    SellerOrdersComponent,
    ProductManageComponent
  ],
  imports: [
    CommonModule,
    SellerRoutingModule,
    ReactiveFormsModule,
    FormsModule
  ]
})
export class SellerModule { }
