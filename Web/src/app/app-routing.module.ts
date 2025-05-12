import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from './core/auth.guard';
import { AdminGuard } from './core/admin.guard';

const routes: Routes = [
  {
    path: 'auth',
    loadChildren: () =>
      import('./features/auth/auth.module').then((m) => m.AuthModule),
  },
  {
    path: 'user',
    canActivate: [AuthGuard],
    loadChildren: () =>
      import('./features/user/user.module').then((m) => m.UserModule),
  },
  {
    path: 'admin',
    canActivate: [AdminGuard],
    loadChildren: () =>
      import('./features/admin/admin.module').then((m) => m.AdminModule)
  },
  { path: '', redirectTo: 'auth/login', pathMatch: 'full' },
  { path: 'seller', loadChildren: () => import('./features/seller/seller.module').then(m => m.SellerModule) },
  { path: '**', redirectTo: 'auth/login' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
