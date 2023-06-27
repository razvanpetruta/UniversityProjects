import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthenticateComponent } from './components/authenticate/authenticate.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { NeighboursComponent } from './components/neighbours/neighbours.component';
import { FinalrouteComponent } from './components/finalroute/finalroute.component';

const routes: Routes = [
  {
    path: 'dashboard',
    component: DashboardComponent
  },
  {
    path: 'finalroute',
    component: FinalrouteComponent
  },
  {
    path: 'neighbours/:id',
    component: NeighboursComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
