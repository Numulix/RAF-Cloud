import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CreateMachineComponent } from './components/create-machine/create-machine.component';
import { CreateUserComponent } from './components/create-user/create-user.component';
import { EditUserComponent } from './components/edit-user/edit-user.component';
import { ErrorLogsComponent } from './components/error-logs/error-logs.component';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { MachinesComponent } from './components/machines/machines.component';
import { AuthGuard } from './guards/auth.guard';

const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    component: HomeComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'login',
    component: LoginComponent,
  },
  {
    path: 'create',
    component: CreateUserComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'edit/:id',
    component: EditUserComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'machines',
    component: MachinesComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'create-machine',
    component: CreateMachineComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'logs',
    component: ErrorLogsComponent,
    canActivate: [AuthGuard]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
