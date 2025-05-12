import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './dashboard/dashboard.component';
import { AdminGuard } from '@app/core/admin.guard';
import { ManageUserComponent } from './manage-user/manage-user.component';
import { ManageProductComponent } from './products/manage-product/manage-product.component';
import { ManageOrdersComponent } from './orders/manage-orders/manage-orders.component';
const routes: Routes = [
  { path: 'dashboard', component: DashboardComponent },
  {
    path: '',
    component: DashboardComponent,
    canActivate: [AdminGuard]
  },
  { path: 'users', component: ManageUserComponent },
  { path: 'products', component: ManageProductComponent },
  { path: 'orders', component: ManageOrdersComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminRoutingModule { }
