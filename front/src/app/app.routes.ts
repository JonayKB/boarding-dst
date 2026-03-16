import { Routes } from '@angular/router';
import { CarsView } from './features/CarsView';
import { LoginView } from './features/LoginView';
import { isAuthenticated, requiredRole } from './core/guard/auth.guard';
import { ForbiddenView } from './features/ForbiddenView';
import { AddCarView } from './features/AddCarView';

export const routes: Routes = [
    {
        path: '',
        redirectTo: 'cars',
        pathMatch: 'full'
    },
    {
        path: 'cars',
        component: CarsView,
        canActivate: [isAuthenticated()]
    },
    {
        path: 'forbidden',
        component: ForbiddenView
    },
    {
        path:'cars/add',
        component: AddCarView,
        canActivate: [isAuthenticated(), requiredRole('ROLE_ADMIN')]
    },
    {
        path: 'login',
        component: LoginView
    }
];