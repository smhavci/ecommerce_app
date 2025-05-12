import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AdminRoutingModule } from './admin-routing.module';
import { DashboardComponent } from './dashboard/dashboard.component';
import { ManageUserComponent } from './manage-user/manage-user.component';
import { FormsModule } from '@angular/forms';
import { ManageProductComponent } from './products/manage-product/manage-product.component';
import { ReactiveFormsModule } from '@angular/forms';
import { ManageOrdersComponent } from './orders/manage-orders/manage-orders.component';


@NgModule({
  declarations: [
    DashboardComponent,
    ManageUserComponent,
    ManageProductComponent,
    ManageOrdersComponent
  ],
  imports: [
    CommonModule,
    AdminRoutingModule,
    FormsModule,
    ReactiveFormsModule
  ]
})
export class AdminModule { }
