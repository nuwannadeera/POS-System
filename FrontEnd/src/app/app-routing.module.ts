import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {SidebarComponent} from './components/sidebar/sidebar.component';
import {DashboardComponent} from './components/dashboard/dashboard.component';
import {CustomerComponent} from './components/customer/customer.component';
import {ItemComponent} from './components/item/item.component';


const routes: Routes = [
  {
    path: '', component: SidebarComponent
  },
  // {
  //   path: '', component: DashboardComponent
  // },
  {
    path: 'dashboard', component: DashboardComponent
  },
  {
    path: 'customer', component: CustomerComponent
  },
  {
    path: 'fruititem', component: ItemComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
