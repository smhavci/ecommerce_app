import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ProductCreateComponent } from './product-create/product-create.component';
import { SellerGuard } from '../../core/seller.guard'; // SellerGuard'ı buraya import et
import { SellerProfileComponent } from './seller-profile/seller-profile.component';
import { SellerComponent } from './seller.component';
import { SellerOrdersComponent } from './seller-orders/seller-orders.component';
import { ProductManageComponent } from './product-manage/product-manage.component';

const routes: Routes = [
  { path: 'product-create', component: ProductCreateComponent, canActivate: [SellerGuard] },
  { path: '', component: SellerComponent },
  { path: 'profile', component: SellerProfileComponent },
  { path: 'orders', component: SellerOrdersComponent }, // Seller siparişler sayfası
  { path: 'my-products', component: ProductManageComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class SellerRoutingModule {}
